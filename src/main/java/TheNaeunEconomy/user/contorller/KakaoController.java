//package TheNaeunEconomy.user.contorller;
//
//
//import TheNaeunEconomy.user.Repository.UserRepository;
//import TheNaeunEconomy.user.domain.User;
//import TheNaeunEconomy.user.service.KakaoService;
//import TheNaeunEconomy.user.service.UserService;
//import TheNaeunEconomy.user.service.UserServiceImpl;
//import java.util.Map;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("/oauth/kakao/login")
//public class KakaoController {
//    private final KakaoService kakaoService;
//    private final UserServiceImpl userService;
//
//    @Value("${kakao.client-id}")
//    private String client_id;
//
//    @Value("${kakao.redirect-uri}")
//    private String redirect_uri;
//
//    @GetMapping("/page")
//    public ModelAndView loginPage(Model model) {
//        String location =
//                "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri="
//                        + redirect_uri;
//        model.addAttribute("location", location);
//
//        return new ModelAndView("login");
//    }
//
//    @GetMapping("/callback")
//    public ResponseEntity<?> callback(@RequestParam("code") String code) {
//        String accessToken = kakaoService.getAccessTokenFromKakao(code);
//
//        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
//        String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");
//        String name = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");
//
//        if (email == null) {
//            return ResponseEntity.badRequest().body("Email not found in Kakao account.");
//        }
//
//        Optional<User> existingUser = userService.findUserByEmail(email);
//
//        if (existingUser.isPresent()) {
//            log.info("User already exists: {}", existingUser.get());
//            return ResponseEntity.ok("User logged in successfully.");
//        } else {
//            User newUser = userService.registerUser(email, name);
//            log.info("New user registered: {}", newUser);
//            return ResponseEntity.ok("User registered successfully.");
//        }
//    }
//}