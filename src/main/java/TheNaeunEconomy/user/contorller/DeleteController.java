package TheNaeunEconomy.user.contorller;

import TheNaeunEconomy.user.service.UserService;
import TheNaeunEconomy.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteController {
    private final UserServiceImpl userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser (@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.deleteUser(token);
        return ResponseEntity.ok("Deleted User");
    }
}
