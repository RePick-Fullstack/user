package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.jwt.TokenProvider;
import TheNaeunEconomy.user.request.LoginUserRequest;
import java.util.Optional;
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

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRequest request) {
        User user = userRepository.getReferenceById(request.getId());
        String string = tokenProvider.generateToken(user);
        return string != null ? ResponseEntity.ok(string) : ResponseEntity.notFound().build();
    }
}