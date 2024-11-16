package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import TheNaeunEconomy.user.service.reponse.AccessTokenResponse;
import TheNaeunEconomy.user.service.reponse.LoginResponse;
import TheNaeunEconomy.user.config.jwt.Token;
import TheNaeunEconomy.user.service.reponse.UserNameResponse;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    @Override
    public ResponseEntity<String> saveUser(AddUserRequest request) {

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });

        User user = new User(request);

        userRepository.save(user);

        return ResponseEntity.ok("회원가입에 성공 하였습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginUserRequest request, HttpServletResponse response) {
            String email = request.getEmail();
            String password = request.getPassword();

            User user = userRepository.findByEmail(email).orElse(null);

            boolean passwordMatch = isPasswordMatch(bCryptPasswordEncoder.encode(password), user.getPassword());

            if (passwordMatch) {
                throw new IllegalArgumentException();
            }

        Token accessToken = tokenProvider.generateToken(user, 15);
        Token refreshToken = tokenProvider.generateToken(user, 60);

        LoginResponse loginResponse = new LoginResponse(accessToken, refreshToken);

            return ResponseEntity.ok().body(loginResponse);
    }


    @Override
    public ResponseEntity<String> logoutUser(String token) {
        return null;
    }

    @Override
    public User updateUser(UpdateUserRequest request, String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        user.updateUserDetails(request);

        return userRepository.save(user);
    }

    public AccessTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {

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
}