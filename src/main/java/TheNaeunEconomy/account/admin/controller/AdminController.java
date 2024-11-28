package TheNaeunEconomy.account.admin.controller;

import TheNaeunEconomy.account.admin.service.AdminServiceImpl;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminServiceImpl adminService;
    private final UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> validateAndReissueAccessToken(@RequestHeader HttpHeaders headers) {
        String refreshToken = getToken(headers, "Bearer");
        return ResponseEntity.ok().body(adminService.refreshToken(refreshToken));
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