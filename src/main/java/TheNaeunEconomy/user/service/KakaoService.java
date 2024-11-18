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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.token-url-host:https://kauth.kakao.com}")
    private String KAUTH_TOKEN_URL_HOST;

    @Value("${kakao.user-url-host:https://kapi.kakao.com}")
    private String KAUTH_USER_URL_HOST;

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponse kakaoTokenResponse = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        if (kakaoTokenResponse == null || kakaoTokenResponse.getAccessToken() == null) {
            throw new RuntimeException("Failed to retrieve access token from Kakao.");
        }

        log.info("Access Token: {}", kakaoTokenResponse.getAccessToken());
        return kakaoTokenResponse.getAccessToken();
    }

    public Map<String, Object> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<Map> response = new RestTemplate().exchange(
                KAUTH_USER_URL_HOST + "/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch user info from Kakao.");
        }
    }
}
