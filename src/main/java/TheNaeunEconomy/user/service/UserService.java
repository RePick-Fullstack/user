package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.service.reponse.UserNameResponse;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public interface UserService {
    ResponseEntity<String> saveUser(AddUserRequest request);

    ResponseEntity<LoginResponse> loginUser(LoginUserRequest request, HttpServletResponse response);

    ResponseEntity<String> logoutUser(String token);

    BodyBuilder updateUser(UpdateUserRequest request, String token);

    BodyBuilder deleteUser(String token);

    UserNameResponse getUserName(String token);
}