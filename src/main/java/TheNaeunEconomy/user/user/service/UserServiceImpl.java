package TheNaeunEconomy.user.user.service;

import TheNaeunEconomy.user.jwt.RefreshTokenRepository;
import TheNaeunEconomy.user.kakao_api.service.request.AccessToken;
import TheNaeunEconomy.user.user.repository.UserRepository;
import TheNaeunEconomy.user.jwt.RefreshToken;
import TheNaeunEconomy.user.user.domain.User;
import TheNaeunEconomy.user.jwt.TokenProvider;
import TheNaeunEconomy.user.user.service.response.LoginResponse;
import TheNaeunEconomy.user.jwt.Token;
import TheNaeunEconomy.user.user.service.response.UserNameResponse;
import TheNaeunEconomy.user.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.kakao_api.service.request.KakaoAccountInfo;
import TheNaeunEconomy.user.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final int ACCESS_TOKEN_TIME = 15;
    private static final int REFRESH_TOKEN_TIME = 60;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    @Override
    public HttpStatus saveUser(AddUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });

        User user = new User(request);

        userRepository.save(user);

        return HttpStatus.OK;
    }

    @Override
    public void registerUser(KakaoAccountInfo kakaoAccountInfo) {
        Optional<User> byEmail = userRepository.findByEmail(kakaoAccountInfo.getEmail());

        if (byEmail.isPresent()) {
            kakaoLoginUser(byEmail.get().getEmail());
        } else {
            userRepository.save(new User(kakaoAccountInfo));
        }
    }

    @Override
    public UserNameResponse getUserName(String token) {
        tokenProvider.validateToken(token);

        Optional<User> user = userRepository.findById(extractUserIdFromToken(token));

        return new UserNameResponse(user.get().getName(), user.get().getNickname());
    }

    @Transactional
    @Override
    public User updateUser(UpdateUserRequest request, String token) {
        Optional<User> user = userRepository.findById(extractUserIdFromToken(token));

        if (user.get().getDeleteDate() != null) {
            throw new NullPointerException();
        }
        user.get().updateUserDetails(request);

        return userRepository.save(user.get());
    }

    @Transactional
    @Override
    public User deleteUser(String token) {
        Optional<User> user = userRepository.findById(extractUserIdFromToken(token));

        user.get().setDeleteDate(LocalDate.from(LocalDateTime.now()));

        return userRepository.save(user.get());
    }

    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }

    @Transactional
    @Override
    public LoginResponse loginUser(LoginUserRequest request, HttpServletResponse response) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.get().getDeleteDate() != null) {
            throw new IllegalStateException("삭제된 사용자입니다.");
        }
        if (!isPasswordMatch(request.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Token accessToken = tokenProvider.generateToken(user.get(), ACCESS_TOKEN_TIME);
        Token refreshToken = tokenProvider.generateToken(user.get(), REFRESH_TOKEN_TIME);

        refreshTokenRepository.save(new RefreshToken(user.get(), refreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_TIME)));

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void logoutUser(String token) {
        refreshTokenRepository.deleteByRefreshToken(token);
    }

    public boolean kakaoUserCheck(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    @Transactional
    public LoginResponse kakaoLoginUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        Token accessToken = tokenProvider.generateToken(user.get(), ACCESS_TOKEN_TIME);
        Token refreshToken = tokenProvider.generateToken(user.get(), REFRESH_TOKEN_TIME);

        refreshTokenRepository.save(new RefreshToken(user.get(), refreshToken.getToken(),
                LocalDateTime.now().plusMinutes(REFRESH_TOKEN_TIME)));

        return new LoginResponse(accessToken, refreshToken);
    }

    public AccessToken refreshToken(String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken);
        refreshTokenRepository.updateExpirationDateByToken(LocalDateTime.now().plusMinutes(REFRESH_TOKEN_TIME),
                refreshToken);
        return new AccessToken(tokenProvider.validateAndReissueAccessToken(refreshToken));
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    private Long extractUserIdFromToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return tokenProvider.getUserIdFromToken(token);
    }
}