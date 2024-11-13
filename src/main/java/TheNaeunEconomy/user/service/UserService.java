package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User save(AddUserRequest request);

    ResponseEntity<String> login(LoginUserRequest request);

    void logout(String token);

    void updateUser(UpdateUserRequest request, String token);

    void deleteUser(String token);
}