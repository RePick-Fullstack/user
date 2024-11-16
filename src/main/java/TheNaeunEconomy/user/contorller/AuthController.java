package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest request, HttpServletResponse response) {
        return userService.loginUser(request, response);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AddUserRequest request) {
        return userService.saveUser(request);
    }
}