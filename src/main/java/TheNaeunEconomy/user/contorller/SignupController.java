package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SignupController {

    private final UserServiceImpl userService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AddUserRequest request) {
        log.info("AddUserRequest = {}", request.toString());
        userService.save(request);
        return ResponseEntity.ok("User registered successfully.");
    }
}