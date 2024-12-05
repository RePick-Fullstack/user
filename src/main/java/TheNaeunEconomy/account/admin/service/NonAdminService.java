package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.service.request.LoginAdminRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;

public interface NonAdminService {
    LoginResponse login(LoginAdminRequest loginAdminRequest);
}
