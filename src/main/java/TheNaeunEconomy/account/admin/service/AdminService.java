package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;

public interface AdminService {
    Admin saveAdmin(AddAdminRequest request);
}
