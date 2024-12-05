package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.DeleteAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import java.util.List;

public interface SuperAdminService {
    Admin saveAdmin(AddAdminRequest addAdminRequest);

    Admin updateAdmin(String token, UpdateAdminRequest updateAdminRequest);

    void deleteAdmin(String token, DeleteAdminRequest deleteAdminRequest);

    List<Admin> findAllAdmin();
}