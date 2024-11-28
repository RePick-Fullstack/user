package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.user.service.response.UserNameResponse;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.account.kakao_api.service.request.KakaoAccountInfo;
import TheNaeunEconomy.account.user.service.request.LoginUserRequest;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User saveUser(AddUserRequest request);

    LoginResponse loginUser(LoginUserRequest request);

    void logoutUser(String token);

    User updateUser(UpdateUserRequest request, String token);

    User deleteUser(String token);

    UserNameResponse getUserName(String token);

    LoginResponse registerUser(KakaoAccountInfo kakaoAccountInfo);

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);

    User deactivateUserId(Long userId);

    Map<String, Long> getUsersCountByMonth();

    Map<String, Long> countDeletedUsersByMonthNative();
}