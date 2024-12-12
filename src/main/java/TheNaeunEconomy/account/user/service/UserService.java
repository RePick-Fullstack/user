package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.naverapi.service.request.NaverAccountInfo;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.kakaoapi.service.request.KakaoAccountInfo;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.account.user.service.response.UserMyPageResponse;
import TheNaeunEconomy.account.user.service.response.UserNickNameResponse;
import TheNaeunEconomy.jwt.Token;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

public interface UserService {
    void logoutUser(String token);

    User updateUser(UpdateUserRequest request, String token);

    User deleteUser(String token);

    UserNickNameResponse getUserName(String token);

    LoginResponse registerKakaoUser(KakaoAccountInfo kakaoAccountInfo);

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

    Token getUserToken(Long userId);

    List<Object[]> getUserBillingCount();

    UserMyPageResponse getUserInfo(String token);

    HttpStatus checkPassword(String token, String password);
}