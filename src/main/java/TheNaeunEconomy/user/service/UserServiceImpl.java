package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.config.jwt.TokenProvider;
import TheNaeunEconomy.user.dto.request.AddUserRequest;
import TheNaeunEconomy.user.dto.request.LoginUserRequest;
import TheNaeunEconomy.user.dto.request.UpdateUserRequest;
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

        return ResponseEntity.ok(token);
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public void updateUser(UpdateUserRequest request) {

    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}