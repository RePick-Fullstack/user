package TheNaeunEconomy.user.kakao_api.service;

import TheNaeunEconomy.user.kakao_api.service.response.KakaoTokenResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url:https://kauth.kakao.com/oauth/token}")
    private String tokenUrl;

    @Value("${kakao.user-url:https://kapi.kakao.com/v2/user/me}")
    private String userUrl;

    public String getAccessTokenFromKakao(String code) {
        WebClient webClient = WebClient.create();
        KakaoTokenResponse kakaoTokenResponse = webClient.post()
                .uri(tokenUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .bodyValue("grant_type=authorization_code" +
                        "&client_id=" + clientId +
                        "&redirect_uri=" + redirectUri +
                        "&code=" + code)
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        if (kakaoTokenResponse == null || kakaoTokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Failed to retrieve access token from Kakao.");
        }

        return kakaoTokenResponse.getAccessToken();
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                userUrl,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("Failed to retrieve user info from Kakao.");
        }

        return response.getBody();
    }
}
