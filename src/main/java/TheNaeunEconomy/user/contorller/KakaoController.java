package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.KakaoService;
import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.service.request.KakaoAccountInfo;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth/kakao/login")
public class KakaoController {
    private final KakaoService kakaoService;
    private final UserServiceImpl userService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private String clientRedirectUrl = "http://localhost:5173";

    @GetMapping("/callback")
    public ResponseEntity<LoginResponse> callback(@RequestParam("code") String code) {
        log.info("Authorization Code: {}", code);

        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        log.info("User Info: {}", userInfo);

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        if (kakaoAccount == null) {
            log.error("kakao_account not found.");
            return ResponseEntity.badRequest().body(null);
        }

        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            log.error("Failed to retrieve email from kakao_account.");
            return ResponseEntity.badRequest().body(null);
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = profile != null ? (String) profile.get("nickname") : "Unknown";

        String birthyear = (String) kakaoAccount.get("birthyear");
        if (birthyear == null) {
            log.warn("Birthyear not available in kakao_account.");
        }

        String birthday = (String) kakaoAccount.get("birthday");
        if (birthday == null) {
            log.warn("Birthday not available in kakao_account.");
        }

        String gender = (String) kakaoAccount.get("gender");
        if (gender == null) {
            log.warn("Gender not available in kakao_account.");
        }

        KakaoAccountInfo kakaoAccountInfo = new KakaoAccountInfo(email, nickname, birthyear, birthday, gender);

        userService.registerUser(kakaoAccountInfo);
        return userService.kakaoLoginUser(email);
    }
}
