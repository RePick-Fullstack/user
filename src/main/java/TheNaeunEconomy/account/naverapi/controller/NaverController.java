package TheNaeunEconomy.account.naverapi.controller;

import TheNaeunEconomy.account.naverapi.service.NaverService;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/oauth/naver")
public class NaverController {

    private final NaverService naverService;

    @GetMapping("/callback")
    public void naverCallback(@RequestParam("code") String code, @RequestParam("state") String state,
                                       HttpServletResponse response) throws Exception {
        response.sendRedirect(redirectExistingUser(naverService.handleNaverLogin(code, state)));
    }

    private String redirectExistingUser(LoginResponse loginResponse) {
        return String.format("http://localhost:5173/?accessToken=%s&refreshToken=%s",
                URLEncoder.encode(loginResponse.getAccessToken().getToken(), StandardCharsets.UTF_8),
                URLEncoder.encode(loginResponse.getRefreshToken().getToken(), StandardCharsets.UTF_8));
    }
}