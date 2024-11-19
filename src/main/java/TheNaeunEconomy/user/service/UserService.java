package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.service.reponse.UserNameResponse;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.KakaoAccountInfo;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public interface UserService {


    HttpStatus saveUser(AddUserRequest request);

    ResponseEntity<LoginResponse> loginUser(LoginUserRequest request, HttpServletResponse response);

    BodyBuilder logoutUser(String token);

    User updateUser(UpdateUserRequest request, String token);

    User deleteUser(String token);

    UserNameResponse getUserName(String token);

    HttpStatus registerUser(KakaoAccountInfo kakaoAccountInfo);
}