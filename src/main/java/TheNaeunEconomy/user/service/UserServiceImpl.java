package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.user.util.NicknameGenerator;
import jakarta.annotation.Nullable;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;


    @Transactional
    @Override
    public ResponseEntity<String> saveUser(AddUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호가 틀려요.");
        }

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });

        String encodePassword = bCryptPasswordEncoder.encode(request.getPassword());

        request.setNickname(request.getNickname().isEmpty() ? NicknameGenerator.generate() : request.getNickname());

        User user = new User(request, encodePassword);

        userRepository.save(user);

        return ResponseEntity.ok("회원가입에 성공 하였습니다.");
    }

    @Transactional
    @Override
    public ResponseEntity<String> loginUser(LoginUserRequest request, HttpServletResponse response) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);
        ResponseEntity<String> body = validateStringResponseEntity(user, password);
        if (body != null) {
            return body;
        }

        String accessToken = tokenProvider.generateToken(user, 15);
        String refreshToken = tokenProvider.generateToken(user, 60);

        response.setHeader("ACCESS-TOKEN", accessToken);
        response.setHeader("REFRESH-TOKEN", refreshToken);

        log.info("accessToken : {}", accessToken);
        log.info("refreshToken : {}", refreshToken);

        return ResponseEntity.ok("로그인이 성공적으로 완료되었습니다.");
    }


    @Override
    public ResponseEntity<String> logoutUser(String token) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateUser(UpdateUserRequest request, String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        if (user.getIsDeleted()) {
            return ResponseEntity.status(404).body("당신은 비활성화 계정 처리가 되어 있습니다. 관리자한테 문의하세요.");
        }

        user.updateUserDetails(request);
        userRepository.save(user);
        return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
    }

    @Override
    public ResponseEntity<String> deleteUser(String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        user.setIsDeleted(true);
        user.setDeleteDate(LocalDate.from(LocalDateTime.now()));

        userRepository.save(user);
        log.info("User with UUID: {} marked as deleted.", userUuidFromToken);
        return ResponseEntity.ok("유저는 3개월 후 삭제 처리 됩니다.");
    }

    @Override
    public ResponseEntity<String> getUserName(String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        String name = user.getName();
        return ResponseEntity.ok(name);
    }

    @Override
    public ResponseEntity<String> getUserNickname(String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        String nickname = user.getNickname();
        return ResponseEntity.ok(nickname);
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

    @Nullable
    private ResponseEntity<String> validateStringResponseEntity(User user, String password) {
        if (user == null) {
            return ResponseEntity.status(404).body("존재하지 않는 사용자 입니다.");
        }

        if (!isPasswordMatch(password, user.getPassword())) {
            return ResponseEntity.status(404).body("비밀번호가 틀렸습니다.");
        }

        if (user.getIsDeleted()) {
            return ResponseEntity.status(404).body("당신은 비활성화 계정 처리가 되어 있습니다. 관리자한테 문의하세요.");
        }
        return null;
    }
}