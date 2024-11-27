package TheNaeunEconomy.account.admin.controller;


import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.service.AdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/signup")
    public ResponseEntity<Admin> createAdmin(@RequestBody @Valid AddAdminRequest request) {
        return ResponseEntity.ok().body(adminService.saveAdmin(request));
    }

    @PutMapping("/update")
    public ResponseEntity<Admin> updateAdmin(@RequestBody @Valid UpdateAdminRequest request,
                                             @RequestHeader HttpHeaders headers) {
        String token = getToken(headers, "Bearer");
        return ResponseEntity.ok().body(adminService.updateAdmin(request, token));
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
