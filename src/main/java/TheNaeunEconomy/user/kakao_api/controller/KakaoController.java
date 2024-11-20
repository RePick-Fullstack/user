package TheNaeunEconomy.user.kakao_api.controller;


import TheNaeunEconomy.user.kakao_api.service.KakaoService;
import TheNaeunEconomy.user.user.service.UserServiceImpl;
import TheNaeunEconomy.user.user.service.response.LoginResponse;
import TheNaeunEconomy.user.kakao_api.service.request.KakaoAccountInfo;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        String birthYear = (String) kakaoAccount.get("birthyear");
        String birthDay = (String) kakaoAccount.get("birthday");
        String gender = (String) kakaoAccount.get("gender");

        KakaoAccountInfo kakaoAccountInfo = new KakaoAccountInfo(email, nickname, birthYear, birthDay, gender);

        userService.registerUser(kakaoAccountInfo);
        LoginResponse loginResponse = userService.kakaoLoginUser(email);
        String redirectUrl = "http://localhost:5173/?accessToken=" + loginResponse.getAccessToken().getToken()
                + "&refreshToken=" + loginResponse.getRefreshToken().getToken();

        response.sendRedirect(redirectUrl);
    }
}