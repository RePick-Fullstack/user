package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.KakaoService;
import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
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
        // 1. 카카오에서 Access Token 가져오기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 2. Access Token으로 사용자 정보 가져오기
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        log.info("userInfo: {}", userInfo);  // userInfo 구조 확인

        // 3. "kakao_account"가 null인지 확인
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        if (kakaoAccount == null) {
            log.error("kakao_account not found in userInfo.");
            return ResponseEntity.badRequest().body(null);  // 오류 처리
        }

        // 4. 이메일 추출
        String email = (String) kakaoAccount.get("email");
        if (email == null) {
            log.error("Email not found in kakao_account.");
            return ResponseEntity.badRequest().body(null);  // 이메일이 없는 경우 처리
        }

        // 5. 프로필 정보 추출 (profile -> nickname)
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile == null) {
            log.error("Profile not found in kakao_account.");
            return ResponseEntity.badRequest().body(null);  // 프로필이 없는 경우 처리
        }

        String name = (String) profile.get("nickname");
        if (name == null) {
            log.error("Nickname not found in profile.");
            return ResponseEntity.badRequest().body(null);  // 닉네임이 없는 경우 처리
        }

        // 6. 로그 출력 (확인용)
        log.info("Email: {}", email);
        log.info("Name: {}", name);

        // 7. 사용자 등록 및 로그인 처리
        userService.registerUser(email, name);
        return userService.kakaoLoginUser(email);
    }
}