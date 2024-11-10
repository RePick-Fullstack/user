package TheNaeunEconomy.user.contorller;


import TheNaeunEconomy.user.request.AddUserRequest;
import TheNaeunEconomy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class SignupController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user")
    public String signup(@RequestBody AddUserRequest request) {
        userService.save(request);
        return "redirect:/login";
    }


}