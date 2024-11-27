package TheNaeunEconomy.account.admin.controller;

import TheNaeunEconomy.account.admin.service.AdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminServiceImpl adminService;

    @PostMapping("/create")
    public void createAdmin(AddAdminRequest request) {
        adminService.saveAdmin(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAdmin(@RequestBody LoginAdminRequest loginAdminRequest) {
        return ResponseEntity.ok().body(adminService.login(loginAdminRequest));
    }
}
