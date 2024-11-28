package TheNaeunEconomy.account.kakao_api.controller;


import TheNaeunEconomy.account.kakao_api.service.KakaoService;
import TheNaeunEconomy.account.kakao_api.service.request.KakaoAccountInfo;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/oauth/kakao")
public class KakaoController {
    private final KakaoService kakaoService;
    private final UserServiceImpl userService;

    @SneakyThrows
    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, HttpServletResponse response) {
        String accessToken = kakaoService.getAccessToken(code);
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = profile != null ? (String) profile.get("nickname") : "Unknown";

        if (userService.kakaoUserCheck(email)) {
            LoginResponse loginResponse = userService.kakaoLoginUser(email);
            String redirectUrl =
                    "http://localhost:5173/?accessToken=" + loginResponse.getAccessToken().getToken() + "&refreshToken="
                            + loginResponse.getRefreshToken().getToken();
            response.sendRedirect(redirectUrl);
        } else {
            String redirectUrl =
                    "http://localhost:5173/complete-profile?email=" + URLEncoder.encode(email, "UTF-8") + "&name="
                            + URLEncoder.encode(nickname, "UTF-8");
            response.sendRedirect(redirectUrl);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody @Valid KakaoAccountInfo kakaoAccountInfo) {
        return ResponseEntity.ok().body(userService.registerUser(kakaoAccountInfo));
    }
}