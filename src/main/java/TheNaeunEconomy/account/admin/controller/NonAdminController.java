package TheNaeunEconomy.account.admin.controller;

import TheNaeunEconomy.account.admin.service.NonAdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class NonAdminController {
    private final NonAdminServiceImpl nonAdminService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody @Valid LoginAdminRequest loginAdminRequest) {
        return ResponseEntity.ok().body(nonAdminService.login(loginAdminRequest));
    }
}
