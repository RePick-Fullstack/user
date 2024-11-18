package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.RefreshTokenRepository;
import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.RefreshToken;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import TheNaeunEconomy.user.service.reponse.AccessTokenResponse;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.domain.Token;
import TheNaeunEconomy.user.service.reponse.UserNameResponse;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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


    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(String email, String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        User user = new User(email, name);
        return userRepository.save(user);
    }

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

    @Transactional
    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginUserRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이메일입니다."));

        if (user.getDeleteDate() != null) {
            throw new IllegalStateException("삭제된 사용자입니다.");
        }

        if (!isPasswordMatch(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Token accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_TIME);
        Token refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_TIME);

        refreshTokenRepository.save(
                new RefreshToken(user, refreshToken.getToken(), LocalDateTime.now().plusMinutes(REFRESH_TOKEN_TIME)));

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }

    @Transactional
    @Override
    public BodyBuilder logoutUser(String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        refreshTokenRepository.findByUserId(user.getId()).ifPresent(refreshToken -> {
            refreshTokenRepository.delete(refreshToken);
        });

        return ResponseEntity.ok();
    }


    @Override
    public User updateUser(UpdateUserRequest request, String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        if (user.getDeleteDate() != null) {
            throw new NullPointerException();
        }

        user.updateUserDetails(request);

        return userRepository.save(user);
    }

    public AccessTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        String userUuidFromToken = extractUserUuidFromToken(refreshToken);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        Optional<RefreshToken> byUserId = refreshTokenRepository.findByUserId(user.getId());

        if (byUserId.isEmpty()) {
            throw new NullPointerException();
        }

        Token newAccessToken = tokenProvider.validateAndReissueAccessToken(refreshToken);

        return new AccessTokenResponse(newAccessToken);
    }

    @Override
    public User deleteUser(String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        //수정해야 함.
        user.setDeleteDate(LocalDate.from(LocalDateTime.now()));

        return userRepository.save(user);
    }

    @Override
    public UserNameResponse getUserName(String token) {
        tokenProvider.validateToken(token);
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        return new UserNameResponse(user.getName(), user.getNickname());
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    private String extractUserUuidFromToken(String token) {
        if (!tokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        return tokenProvider.getUserUuidFromToken(token);
    }

    public ResponseEntity<LoginResponse> kakaoLoginUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이메일입니다."));

        if (user.getDeleteDate() != null) {
            throw new IllegalStateException("삭제된 사용자입니다.");
        }

        Token accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_TIME);
        Token refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_TIME);

        refreshTokenRepository.save(
                new RefreshToken(user, refreshToken.getToken(), LocalDateTime.now().plusMinutes(REFRESH_TOKEN_TIME)));

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
    }
}
