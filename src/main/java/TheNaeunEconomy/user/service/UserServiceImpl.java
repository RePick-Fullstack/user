package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.user.util.NicknameGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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
    public User saveUser(AddUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호가 틀려요.");
        }
        if (request.getNickname().equals("")){
            request.setNickname(NicknameGenerator.generate());
        }


        User user = new User(request.getEmail(), bCryptPasswordEncoder.encode(request.getPassword()), request.getName(),
                request.getNickname(), request.getGender(), request.getBirthDate());

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public ResponseEntity<String> loginUser(LoginUserRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("존재하지 않는 사용자 입니다.");
        }

        if (!isPasswordMatch(password, user.getPassword())) {
            return ResponseEntity.status(404).body("비밀번호가 틀렸습니다.");
        }

        if (user.getIsDeleted()) {
            return ResponseEntity.status(404).body("당신은 비활성화 계정 처리가 되어 있습니다. 관리자한테 문의하세요.");
        }

        String token = tokenProvider.generateToken(user);
        log.info("token: {}", token);
        return ResponseEntity.ok(token);
    }

    @Override
    public void logoutUser(String token) {
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
    public void deleteUser(String token) {
        String userUuidFromToken = extractUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        user.setIsDeleted(true);
        user.setDeleteDate(LocalDate.from(LocalDateTime.now()));

        userRepository.save(user);
        log.info("User with UUID: {} marked as deleted.", userUuidFromToken);
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