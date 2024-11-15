package TheNaeunEconomy.user.contorller;

import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserSettingsController {
    private final UserServiceImpl userService;


    @GetMapping("/name")
    public ResponseEntity<String> getUserName(@RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        return userService.getUserName(token);
    }

    @GetMapping("/nickname")
    public ResponseEntity<String> getUserNickname(@RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        return userService.getUserNickname(token);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        return userService.deleteUser(token);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest request,
                                             @RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer ");
        return userService.updateUser(request, token);
    }

    @Nullable
    private static String getToken(HttpHeaders headers, String Bearer) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith(Bearer)) {
            token = token.substring(7);
        }
        return token;
    }
}