package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.UserService;
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
public class LoginController {

    private final UserServiceImpl userService;

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login.html");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRequest request) {
        return userService.login(request);
    }
}