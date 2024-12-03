package TheNaeunEconomy.account.naverapi.service;

import TheNaeunEconomy.account.naverapi.service.request.NaverAccountInfo;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserServiceImpl userService;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;

    public LoginResponse handleNaverLogin(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .queryParam("state", state);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                null,
                Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();
        Map<String, Object> response = (Map<String, Object>) userInfo.get("response");

        log.info(response.toString());

        String email = (String) response.get("email");
        String name = (String) response.get("name");
        String nickname = (String) response.get("nickname");
        String gender = (String) response.get("gender");
        String year = (String) response.get("birthyear");
        String birthday = (String) response.get("birthday");

        if (userService.naverUserCheck(email)) {
            return userService.naverLoginUser(email);
        } else {
            NaverAccountInfo naverAccountInfo = new NaverAccountInfo(email, name,nickname, gender, year, birthday);
            return userService.registerNaverUser(naverAccountInfo);
        }
    }
}