package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.naverapi.service.request.NaverAccountInfo;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.user.service.response.UserNameResponse;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.account.kakaoapi.service.request.KakaoAccountInfo;
import TheNaeunEconomy.account.user.service.request.LoginUserRequest;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User saveUser(AddUserRequest request);

    LoginResponse loginUser(LoginUserRequest request);

    void logoutUser(String token);

    User updateUser(UpdateUserRequest request, String token);

    User deleteUser(String token);

    UserNameResponse getUserName(String token);

    LoginResponse registerUser(KakaoAccountInfo kakaoAccountInfo);

    boolean naverUserCheck(String email);

    LoginResponse naverLoginUser(String email);

    LoginResponse registerNaverUser(NaverAccountInfo naverAccountInfo);

    Page<User> findAll(Pageable pageable);

    User findByEmail(String email);

    User deactivateUserId(Long userId);

    User activateUserId(Long userId);

    Map<String, Long> getUsersCountByMonth();

    Map<String, Long> countDeletedUsersByMonthNative();

    UserCountResponse getUserCount();

    List<Object[]> getUserGenderCount();
}