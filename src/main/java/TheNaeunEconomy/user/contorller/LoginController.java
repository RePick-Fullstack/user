package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }
}
