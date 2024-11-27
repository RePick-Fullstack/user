package TheNaeunEconomy.account.admin.controller;


import TheNaeunEconomy.account.admin.service.AdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminServiceImpl adminService;

    @PostMapping("/signup")
    public void createAdmin(AddAdminRequest request) {
        adminService.saveAdmin(request);
    }
}
