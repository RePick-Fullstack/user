package TheNaeunEconomy.account.admin.controller;

import TheNaeunEconomy.account.admin.service.AdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.EmailRequest;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.findAll(pageable));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> validateAndReissueAccessToken(@RequestHeader HttpHeaders headers) {
        String refreshToken = getToken(headers);
        return ResponseEntity.ok().body(adminService.refreshToken(refreshToken));
    }

    @PutMapping("/users/deactivate/{userId}")
    public ResponseEntity<User> deactivateUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(adminService.deactivateUserId(userId));
    }

    @PutMapping("/users/activate/{userId}")
    public ResponseEntity<User> activateUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(adminService.activateUserId(userId));
    }

    @GetMapping("/users/month")
    public Map<String, Long> getUsersMonth() {
        return adminService.getUsersCountByMonth();
    }

    @GetMapping("/users/count")
    public ResponseEntity<UserCountResponse> getUserCount() {
        return ResponseEntity.ok().body(adminService.getUserCount());
    }

    @GetMapping("/users/gender/count")
    public ResponseEntity<List<Object[]>> getUserGenderCount() {
        return ResponseEntity.ok().body(adminService.getUserGenderCount());
    }

    @GetMapping("/users/delete")
    public Map<String, Long> getUsersDeleted() {
        return adminService.countDeletedUsersByMonthNative();
    }

    @PostMapping("/users/email")
    public ResponseEntity<User> getUsersByEmail(@RequestBody @Valid EmailRequest email) {
        return ResponseEntity.ok().body(adminService.findByEmail(email.getEmail()));
    }

    @Nullable
    private static String getToken(HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);
        }
        return token;
    }
}