package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.repository.AdminRepository;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.DeleteAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import TheNaeunEconomy.jwt.TokenProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final AdminRepository adminRepository;
    private final TokenProvider tokenProvider;

    @Override
    public Admin saveAdmin(AddAdminRequest addAdminRequest) {
        return adminRepository.save(new Admin(addAdminRequest));
    }

    @Override
    public Admin updateAdmin(String token, UpdateAdminRequest request) {
        Admin admin = adminRepository.findById(request.getAdminId()).orElseThrow();
        admin.updateUserDetails(request);
        return adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(String token, DeleteAdminRequest request) {
        tokenProvider.validateToken(token);
        adminRepository.deleteById(request.getAdminId());
    }

    @Override
    public List<Admin> findAllAdmin() {
        return adminRepository.findAll();
    }
}
