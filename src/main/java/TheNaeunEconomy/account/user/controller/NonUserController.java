package TheNaeunEconomy.account.user.controller;


import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.account.user.service.request.LoginUserRequest;
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
public class NonUserController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        return ResponseEntity.ok().body(userService.loginUser(loginUserRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody AddUserRequest request) {
        return ResponseEntity.ok().body(userService.saveUser(request));
    }
}