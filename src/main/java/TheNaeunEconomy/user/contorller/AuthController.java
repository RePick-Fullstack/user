package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final UserServiceImpl userService;

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response) {
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        return userService.loginUser(loginUserRequest , response);
    }

    @PostMapping("/signup")
    public HttpStatus registerUser(@Valid @RequestBody AddUserRequest request) {
        return userService.saveUser(request);
    }
}