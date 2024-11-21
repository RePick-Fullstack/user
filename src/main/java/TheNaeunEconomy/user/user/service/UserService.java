package TheNaeunEconomy.user.user.service;

import TheNaeunEconomy.user.user.domain.User;
import TheNaeunEconomy.user.user.service.response.LoginResponse;
import TheNaeunEconomy.user.user.service.response.UserNameResponse;
import TheNaeunEconomy.user.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.kakao_api.service.request.KakaoAccountInfo;
import TheNaeunEconomy.user.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public interface UserService {

    HttpStatus saveUser(AddUserRequest request);

    LoginResponse loginUser(LoginUserRequest request, HttpServletResponse response);

    void logoutUser(String token);

    User updateUser(UpdateUserRequest request, String token);

    User deleteUser(String token);

    UserNameResponse getUserName(String token);

    void registerUser(KakaoAccountInfo kakaoAccountInfo);
}