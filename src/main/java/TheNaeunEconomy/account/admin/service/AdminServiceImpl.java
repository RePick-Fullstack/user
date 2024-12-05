package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserServiceImpl userService;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        return userService.refreshToken(refreshToken);
    }

    @Override
    public User deactivateUserId(Long userId) {
        return userService.deactivateUserId(userId);
    }

    @Override
    public User activateUserId(Long userId) {
        return userService.activateUserId(userId);
    }

    @Override
    public Map<String, Long> getUsersCountByMonth() {
        return userService.getUsersCountByMonth();
    }

    @Override
    public UserCountResponse getUserCount() {
        return userService.getUserCount();
    }

    @Override
    public List<Object[]> getUserGenderCount() {
        return userService.getUserGenderCount();
    }

    @Override
    public Map<String, Long> countDeletedUsersByMonthNative() {
        return userService.countDeletedUsersByMonthNative();
    }

    @Override
    public User findByEmail(String email) {
        return userService.findByEmail(email);
    }
}