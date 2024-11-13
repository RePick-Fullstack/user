package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRequest request) {
        log.info("Login user token: {}", userService.login(request));
        return userService.login(request);
    }
}