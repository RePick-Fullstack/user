package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.domain.UserActivityLog;
import TheNaeunEconomy.account.user.domain.UserSuggestions;
import TheNaeunEconomy.account.user.repository.UserActivityLogRepository;
import TheNaeunEconomy.account.user.repository.UserRepository;
import TheNaeunEconomy.account.user.repository.UserSuggestionsRepository;
import TheNaeunEconomy.account.user.service.request.AddSuggestionsRequest;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.account.user.service.request.LoginUserRequest;
import TheNaeunEconomy.account.user.service.response.LoginResponse;
import TheNaeunEconomy.jwt.RefreshTokenRepository;
import TheNaeunEconomy.jwt.Token;
import TheNaeunEconomy.jwt.TokenProvider;
import TheNaeunEconomy.jwt.domain.RefreshToken;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class NonUserServiceImpl implements NonUserService {

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

    @Override
    public LoginResponse loginUser(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

        validateUser(user, request.getPassword());

        userActiveLog(user);

        Token accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_MINUTE_TIME);
        Token refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_MINUTE_TIME);

        refreshTokenRepository.save(new RefreshToken(user, refreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_MINUTE_TIME)));

        return new LoginResponse(accessToken, refreshToken);
    }

    private void userActiveLog(User user) {
        Optional<UserActivityLog> existingLog = userActivityLogRepository.findByUserIdAndActivityDate(user.getId(), LocalDate.now());

        if (existingLog.isEmpty()) {
            UserActivityLog log = new UserActivityLog();
            log.setUserId(user.getId());
            log.setActivityDate(LocalDate.now());
            userActivityLogRepository.save(log);
        }
    }

    @Override
    public User saveUser(AddUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });
        return userRepository.save(new User(request));
    }

    @Override
    public String saveSuggestions(AddSuggestionsRequest request) {
        UserSuggestions suggestion = new UserSuggestions(request.getName(), request.getContent());
        userSuggestionsRepository.save(suggestion);
        return "관리자한테 전달했습니다.";
    }



    private void validateUser(User user, String rawPassword) {
        if (user.getDeleteDate() != null) {
            throw new IllegalStateException("정지된 사용자입니다. 관리자에게 문의하세요.");
        }

        if (!isPasswordMatch(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}