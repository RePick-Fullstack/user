package TheNaeunEconomy.user.contorller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    @GetMapping("/")
    public ModelAndView mainPage() {
        return new ModelAndView("main.html");
    }
}