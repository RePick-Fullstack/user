package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> saveUser(AddUserRequest request);

    ResponseEntity<String> loginUser(LoginUserRequest request, HttpServletResponse response);

    ResponseEntity<String> logoutUser(String token);

    ResponseEntity<String> updateUser(UpdateUserRequest request, String token);

    ResponseEntity<String> deleteUser(String token);

    ResponseEntity<String> getUserName(String token);
    ResponseEntity<String> getUserNickname(String token);
}