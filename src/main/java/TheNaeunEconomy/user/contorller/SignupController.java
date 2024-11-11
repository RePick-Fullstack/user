package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.request.AddUserRequest;
import TheNaeunEconomy.user.service.UserService;
import jakarta.validation.Valid;
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
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AddUserRequest request) {
        log.info("AddUserRequest = {}", request.toString());
        userService.save(request);
        return ResponseEntity.ok("User registered successfully.");
    }
}