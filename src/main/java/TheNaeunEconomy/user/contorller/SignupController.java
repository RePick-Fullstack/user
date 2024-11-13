package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.dto.request.AddUserRequest;
import TheNaeunEconomy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public ModelAndView signupPage() {
        return new ModelAndView("signup");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AddUserRequest request) {
        log.info("AddUserRequest = {}", request.toString());
        userService.save(request);
        return ResponseEntity.ok("User registered successfully.");
    }
}