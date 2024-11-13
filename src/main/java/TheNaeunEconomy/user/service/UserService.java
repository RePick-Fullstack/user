package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.dto.request.AddUserRequest;
import TheNaeunEconomy.user.dto.request.LoginUserRequest;
import TheNaeunEconomy.user.dto.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User save(AddUserRequest request);
    ResponseEntity<String> login(LoginUserRequest request);
    void logout(String token);
    void updateUser(UpdateUserRequest request);
}
