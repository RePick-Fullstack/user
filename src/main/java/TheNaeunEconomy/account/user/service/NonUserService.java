package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.account.user.service.request.LoginUserRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;

public interface NonUserService {
    LoginResponse loginUser(LoginUserRequest request);

    User saveUser(AddUserRequest request);
}
