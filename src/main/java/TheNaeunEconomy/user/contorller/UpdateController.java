package TheNaeunEconomy.user.contorller;

import TheNaeunEconomy.user.service.UserService;
import TheNaeunEconomy.user.service.UserServiceImpl;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UpdateController {
    private final UserServiceImpl userService;

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest request, @RequestHeader HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.updateUser(request, token);
        return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
    }
}