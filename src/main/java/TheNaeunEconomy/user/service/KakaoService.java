package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.service.reponse.KakaoTokenResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
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
    RestTemplate restTemplate = new RestTemplate();
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri; // 설정 파일에서 가져온 redirect-uri

    @Value("${kakao.token-url:https://kauth.kakao.com/oauth/token}")
    private String KAUTH_TOKEN_URL_HOST;

    @Value("${kakao.user-url-host:https://kapi.kakao.com}")
    private String KAUTH_USER_URL_HOST;

    /**
     * Access Token을 가져오는 메서드
     */
    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponse kakaoTokenResponse = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("") // Path는 이미 URL에 포함되어 있음
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("redirect_uri", redirectUri) // redirect_uri 추가
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        if (kakaoTokenResponse == null || kakaoTokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Failed to retrieve access token from Kakao.");
        }

        log.info("Access Token: {}", kakaoTokenResponse.getAccessToken());
        return kakaoTokenResponse.getAccessToken();
    }

    /**
     * 사용자 정보를 가져오는 메서드
     */
    public Map<String, Object> getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }

}
