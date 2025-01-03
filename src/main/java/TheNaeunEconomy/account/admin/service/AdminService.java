package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.domain.UserSuggestions;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.jwt.Token;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Page<User> findAll(Pageable pageable);

    LoginResponse refreshToken(String refreshToken);

    User deactivateUserId(Long userId);

    User activateUserId(Long userId);

    Map<String, Long> getUsersCountByMonth();

    UserCountResponse getUserCount();

    List<Object[]> getUserGenderCount();

    User findByEmail(String email);

    Token getUserToken(Long userId);

    List<Object[]> getUserBillingCount();

    Page<UserSuggestions> getSuggestions(Pageable pageable);
}
