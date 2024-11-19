package TheNaeunEconomy.user.config;

import TheNaeunEconomy.user.config.jwt.JwtAuthenticationFilter;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (JWT 사용 시 불필요)
                .authorizeHttpRequests(auth -> auth
                        // 인증이 필요 없는 경로
                        .requestMatchers(
                                "/",
                                "/api/v1/login",
                                "/api/v1/signup",
                                "/oauth/kakao/login/page",
                                "/oauth/kakao/login/callback"
                        ).permitAll()
                        // 인증이 필요한 경로
                        .requestMatchers("/api/v1/users/**").authenticated()
                        .anyRequest().authenticated()
                )
                // JWT 인증 필터 추가
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // React 개발 서버
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용 메서드
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Access-Control-Allow-Origin")); // 허용 헤더
        configuration.setAllowCredentials(true); // 쿠키 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
