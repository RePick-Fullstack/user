package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.LoginUserRequest;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
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
    public User save(AddUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호가 틀려요.");
        }

        User user = new User(request.getEmail(), bCryptPasswordEncoder.encode(request.getPassword()), request.getName(),
                request.getNickname(), request.getGender(), request.getBirthDate());

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public ResponseEntity<String> login(LoginUserRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("존재하지 않는 사용자 입니다.");
        }

        if (!isPasswordMatch(password, user.getPassword())) {
            return ResponseEntity.status(404).body("비밀번호가 틀렸습니다.");
        }

        String token = tokenProvider.generateToken(user);
        log.info("token: {}", token);
        return ResponseEntity.ok(token);
    }

    @Override
    public void logout(String token) {
    }

    @Override
    public void updateUser(UpdateUserRequest request, String token) {
        tokenProvider.validateToken(token);

        String userUuidFromToken = tokenProvider.getUserUuidFromToken(token);

        User user = userRepository.findByUuid(UUID.fromString(userUuidFromToken))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        user.updateUserDetails(request);

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String token) {
        tokenProvider.validateToken(token);

        String userUuidFromToken = tokenProvider.getUserUuidFromToken(token);

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
}
