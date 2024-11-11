package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.jwt.TokenProvider;
import TheNaeunEconomy.user.request.LoginUserRequest;
import TheNaeunEconomy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRequest request) {
        return userService.login(request);
    }
}