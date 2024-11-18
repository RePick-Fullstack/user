package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.KakaoService;
import TheNaeunEconomy.user.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth/kakao/login")
public class KakaoController {
    private final KakaoService kakaoService;
    private final UserService userService;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private String clientRedirectUrl = "http://localhost:5173";

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam("code") String code) {
        try {
            String accessToken = kakaoService.getAccessTokenFromKakao(code);
            Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
            String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");
            String name = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("name");
            userService.registerUser(email, name);

            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(clientRedirectUrl);
            return redirectView;
        } catch (Exception e) {
            log.error("Authentication failed", e);

            RedirectView errorRedirectView = new RedirectView();
            errorRedirectView.setUrl("/error");
            return errorRedirectView;
        }
    }
}
