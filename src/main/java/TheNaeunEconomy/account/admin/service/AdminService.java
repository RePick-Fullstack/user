package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;

public interface AdminService {
    Admin saveAdmin(AddAdminRequest addAdminRequest);

    LoginResponse login(LoginAdminRequest loginAdminRequest);
}
