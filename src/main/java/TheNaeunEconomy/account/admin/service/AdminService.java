package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.DeleteAdminRequest;
import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import java.util.List;

public interface AdminService {
    Admin saveAdmin(AddAdminRequest addAdminRequest);

    LoginResponse login(LoginAdminRequest loginAdminRequest);

    Admin updateAdmin(String token, UpdateAdminRequest updateAdminRequest);

    void deleteAdmin(String token, DeleteAdminRequest deleteAdminRequest);

    List<Admin> findAllAdmin();
}
