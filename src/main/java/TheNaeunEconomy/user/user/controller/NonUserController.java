package TheNaeunEconomy.user.user.controller;


import TheNaeunEconomy.user.user.service.UserServiceImpl;
import TheNaeunEconomy.user.user.service.response.LoginResponse;
import TheNaeunEconomy.user.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.user.service.request.LoginUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserRequest request, HttpServletResponse response) {
        return ResponseEntity.ok().body(userService.loginUser(request, response));
    }

    @PostMapping("/signup")
    public HttpStatus registerUser(@Valid @RequestBody AddUserRequest request) {
        return userService.saveUser(request);
    }
}