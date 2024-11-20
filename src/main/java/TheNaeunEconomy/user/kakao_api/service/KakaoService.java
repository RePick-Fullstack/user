package TheNaeunEconomy.user.kakao_api.service;

import TheNaeunEconomy.user.kakao_api.service.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private final WebClient webClient;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenUrl;

    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public String getAccessToken(String code) {
        try {
            return webClient.post()
                    .uri(tokenUrl)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", clientId)
                            .with("redirect_uri", redirectUri)
                            .with("code", code))
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block()
                    .getAccessToken();
        } catch (Exception e) {
            log.error("Error while requesting access token", e);
            throw new RuntimeException("Error while requesting access token", e);
        }
    }


    public Map getUserInfo(String accessToken) {
        try {
            Map userInfo = webClient.get()
                    .uri(USER_INFO_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (userInfo == null) {
                throw new RuntimeException("Failed to retrieve user info from Kakao.");
            }
            return userInfo;
        } catch (Exception e) {
            log.error("Error while retrieving user info: {}", e.getMessage(), e);
            throw new RuntimeException("Error while retrieving user info", e);
        }
    }
}
