package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.admin.service.response.UserCountResponse;
import TheNaeunEconomy.account.kakaoapi.service.request.KakaoAccountInfo;
import TheNaeunEconomy.account.naverapi.service.request.NaverAccountInfo;
import TheNaeunEconomy.account.user.Dto.IsBilling;
import TheNaeunEconomy.account.user.Dto.UpdateUserNickName;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.domain.UserActivityLog;
import TheNaeunEconomy.account.user.domain.UserSuggestions;
import TheNaeunEconomy.account.user.repository.UserActivityLogRepository;
import TheNaeunEconomy.account.user.repository.UserRepository;
import TheNaeunEconomy.account.user.repository.UserSuggestionsRepository;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.account.user.service.response.UserMyPageResponse;
import TheNaeunEconomy.account.user.service.response.UserNickNameResponse;
import TheNaeunEconomy.jwt.RefreshTokenRepository;
import TheNaeunEconomy.jwt.Token;
import TheNaeunEconomy.jwt.TokenProvider;
import TheNaeunEconomy.jwt.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${jwt.ACCESS_TOKEN_MINUTE_TIME}")
    private int ACCESS_TOKEN_MINUTE_TIME;
    @Value("${jwt.REFRESH_TOKEN_MINUTE_TIME}")
    private int REFRESH_TOKEN_MINUTE_TIME;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserSuggestionsRepository userSuggestionsRepository;
    private final UserActivityLogRepository userActivityLogRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final KafkaService kafkaService;

    @Override
    public LoginResponse registerKakaoUser(KakaoAccountInfo kakaoAccountInfo) {
        return userRepository.findByEmail(kakaoAccountInfo.getEmail())
                .map(existingUser -> kakaoLoginUser(existingUser.getEmail())).orElseGet(() -> {
                    userRepository.save(new User(kakaoAccountInfo));
                    return kakaoLoginUser(kakaoAccountInfo.getEmail());
                });
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(null);
    }

    @Override
    public UserNickNameResponse getUserName(String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);

        return userRepository.findById(userId).map(user -> new UserNickNameResponse(user.getNickname()))
                .orElseThrow(() -> new IllegalArgumentException("토큰에 대한 사용자를 찾을 수 없습니다. " + token));
    }


    @Override
    public User updateUser(UpdateUserRequest request, String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("토큰에 대한 사용자를 찾을 수 없습니다. " + token));

        if (user.getDeleteDate() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }

        user.updateUserDetails(request);
        UpdateUserNickName updateUserNickName = new UpdateUserNickName(user.getId(), user.getNickname());
        kafkaService.userNickNameUpdate(updateUserNickName);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);
        refreshTokenRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void logoutUser(String token) {
        refreshTokenRepository.deleteByRefreshToken(token);
    }

    public boolean kakaoUserCheck(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    public LoginResponse kakaoLoginUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getDeleteDate() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }
        userActiveLog(user);
        return getLoginResponse(email);
    }

    private LoginResponse getLoginResponse(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

        Token accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
        Token refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(user, refreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        User user = userRepository.findById(tokenProvider.getUserIdFromToken(refreshToken))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Token newAccessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
        Token newRefreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(user, newRefreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));
        System.out.println(new LoginResponse(newAccessToken, newRefreshToken));
        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }


    @Override
    public Map<String, Long> getUsersCountByMonth() {
        List<Object[]> results = userRepository.countUsersByMonth();
        return getStringLongMap(results);
    }

    private Map<String, Long> getStringLongMap(List<Object[]> results) {
        Map<String, Long> monthlyUserCount = new LinkedHashMap<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            monthlyUserCount.put(month, count);
        }
        return monthlyUserCount;
    }

    public UserCountResponse getUserCount() {
        return new UserCountResponse((int) userRepository.count());
    }

    @Override
    public User deactivateUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getDeleteDate() == null) {
            user.setDeleteDate(LocalDate.now());
        }
        return user;
    }

    @Override
    public User activateUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getDeleteDate() != null) {
            user.setDeleteDate(null);
        }
        return user;
    }

    @Override
    public List<Object[]> getUserGenderCount() {
        return userRepository.countUsersByGender();

    }

    @Override
    public Token getUserToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
    }

    @Override
    public List<Object[]> getUserBillingCount() {
        return userRepository.countUserByBilling();
    }

    @Override
    public boolean naverUserCheck(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    @Override
    public LoginResponse naverLoginUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (user.getDeleteDate() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }
        userActiveLog(user);
        return getLoginResponse(email);
    }

    @Override
    public LoginResponse registerNaverUser(NaverAccountInfo naverAccountInfo) {
        return userRepository.findByEmail(naverAccountInfo.getEmail())
                .map(existingUser -> kakaoLoginUser(existingUser.getEmail())).orElseGet(() -> {
                    userRepository.save(new User(naverAccountInfo));
                    return naverLoginUser(naverAccountInfo.getEmail());
                });
    }

    public void updateUserIsBilling(IsBilling isBilling) {
        User user = userRepository.findById(isBilling.getUserId()).orElseThrow();
        user.setIsBilling(isBilling.getIsBilling());
        userRepository.save(user);
    }

    public UserMyPageResponse getUserInfo(String token) {
        Long userIdFromToken = tokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userIdFromToken).orElseThrow();
        return new UserMyPageResponse(user);
    }

    @Override
    public Page<UserSuggestions> findAllSuggestions(Pageable pageable) {
        return userSuggestionsRepository.findAll(pageable);
    }


    private void userActiveLog(User user) {
        Optional<UserActivityLog> existingLog = userActivityLogRepository.findByUserIdAndActivityDate(user.getId(),
                LocalDate.now());

        if (existingLog.isEmpty()) {
            UserActivityLog log = new UserActivityLog();
            log.setUserId(user.getId());
            log.setActivityDate(LocalDate.now());
            userActivityLogRepository.save(log);
        }
    }
}