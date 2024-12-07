package TheNaeunEconomy.account.admin.controller;


import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.service.SuperAdminServiceImpl;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.DeleteAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import TheNaeunEconomy.jwt.TokenProvider;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/super")
public class SuperAdminController {

    private final SuperAdminServiceImpl superAdminService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Admin> createAdmin(@RequestBody @Valid AddAdminRequest request) {
        return ResponseEntity.ok().body(superAdminService.saveAdmin(request));
    }

    @PutMapping("/update")
    public ResponseEntity<Admin> updateAdmin(@RequestHeader HttpHeaders headers,
                                             @RequestBody @Valid UpdateAdminRequest request) {
        String token = tokenProvider.getToken(headers);
        return ResponseEntity.ok().body(superAdminService.updateAdmin(token, request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAdmin(@RequestHeader HttpHeaders headers,
                                              @RequestBody @Valid DeleteAdminRequest request) {
        String token = tokenProvider.getToken(headers);
        superAdminService.deleteAdmin(token, request);
        return ResponseEntity.ok().body("Deleted Admin");
    }

    @GetMapping("/info")
    public ResponseEntity<List<Admin>> info() {
        return ResponseEntity.ok().body(superAdminService.findAllAdmin());
    }
}
