package TheNaeunEconomy.account.admin.service;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.repository.AdminRepository;
import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.jwt.RefreshTokenRepository;
import TheNaeunEconomy.jwt.Token;
import TheNaeunEconomy.jwt.TokenProvider;
import TheNaeunEconomy.jwt.domain.RefreshToken;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;
    @Value("${jwt.REFRESH_TOKEN_MINUTE_TIME}")
    private int REFRESH_TOKEN_MINUTE_TIME;

    private final UserServiceImpl userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AdminRepository adminRepository;
    private final TokenProvider tokenProvider;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        Admin admin = adminRepository.findById(tokenProvider.getAdminIdFromToken(refreshToken))
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다."));

        Token newAccessToken = tokenProvider.generateToken(admin, ACCESS_TOKEN_MINUTE_TIME);
        Token newRefreshToken = tokenProvider.generateToken(admin, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(admin, newRefreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));
        return new LoginResponse(newAccessToken, newRefreshToken);
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

    @Override
    public Token getUserToken(Long userId) {
        return userService.getUserToken(userId);
    }
}