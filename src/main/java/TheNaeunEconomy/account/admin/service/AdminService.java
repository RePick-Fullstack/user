package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
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

    Map<String, Long> countDeletedUsersByMonthNative();

    User findByEmail(String email);
}
