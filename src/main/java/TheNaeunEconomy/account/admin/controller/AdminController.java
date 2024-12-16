package TheNaeunEconomy.account.admin.controller;

import TheNaeunEconomy.account.admin.service.AdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.EmailRequest;
import TheNaeunEconomy.account.admin.service.response.UserCountLoginResponse;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.UserActivityLogServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.jwt.Token;
import TheNaeunEconomy.jwt.TokenProvider;
import jakarta.validation.Valid;
import java.time.LocalDate;
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
    private final UserActivityLogServiceImpl userActivityLogService;
    private final TokenProvider tokenProvider;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> users(Pageable pageable) {
        return ResponseEntity.ok(adminService.findAll(pageable));
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<Token> userToken(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(adminService.getUserToken(userId));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> validateAndReissueAccessToken(@RequestHeader HttpHeaders headers) {
        String refreshToken = tokenProvider.getToken(headers);
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
    public Map<String, Long> usersMonth() {
        return adminService.getUsersCountByMonth();
    }

    @GetMapping("/users/count")
    public ResponseEntity<UserCountResponse> userCount() {
        return ResponseEntity.ok().body(adminService.getUserCount());
    }

    @GetMapping("/users/gender/count")
    public ResponseEntity<List<Object[]>> userGenderCount() {
        return ResponseEntity.ok().body(adminService.getUserGenderCount());
    }

    @GetMapping("/users/billing/count")
    public ResponseEntity<List<Object[]>> userBillingCount() {
        return ResponseEntity.ok().body(adminService.getUserBillingCount());
    }

    @PostMapping("/users/email")
    public ResponseEntity<User> usersByEmail(@RequestBody @Valid EmailRequest email) {
        return ResponseEntity.ok().body(adminService.findByEmail(email.getEmail()));
    }

    @GetMapping("/users/activate/day/count")
    public ResponseEntity<UserCountLoginResponse> activateDayCount() {
        return ResponseEntity.ok().body(userActivityLogService.getDAU(LocalDate.now()));
    }

    @GetMapping("/users/activate/week/count")
    public ResponseEntity<List<UserCountLoginResponse>> activateWeekCount() {
        return ResponseEntity.ok().body(userActivityLogService.getWAU(LocalDate.now()));
    }
}