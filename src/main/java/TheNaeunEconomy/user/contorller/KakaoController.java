package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.KakaoService;
import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.service.request.KakaoAccountInfo;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info("Authorization Code: {}", code);

        // Step 1: Access Token 가져오기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // Step 2: 사용자 정보 가져오기
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        log.info("User Info: {}", userInfo);

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = profile != null ? (String) profile.get("nickname") : "Unknown";
        String birthyear = (String) kakaoAccount.get("birthyear");
        String birthday = (String) kakaoAccount.get("birthday");
        String gender = (String) kakaoAccount.get("gender");

        // 사용자 등록 로직 호출
        KakaoAccountInfo kakaoAccountInfo = new KakaoAccountInfo(email, nickname, birthyear, birthday, gender);
        userService.registerUser(kakaoAccountInfo);

        // 클라이언트로 리다이렉트
        response.sendRedirect("http://localhost:5173?email=" + email);
    }

    @GetMapping()
    public ResponseEntity<LoginResponse> getToken(@RequestParam String email) {
        log.info("Get token: {}", email);
        return userService.kakaoLoginUser(email);
    }
}
