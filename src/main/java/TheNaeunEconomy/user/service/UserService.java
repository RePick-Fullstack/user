package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import TheNaeunEconomy.user.request.AddUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User save(AddUserRequest request) {
        User user = new User(request.getEmail(), bCryptPasswordEncoder.encode(request.getPassword()), request.getName(),
                request.getNickname(), request.getGender(), request.getBirthDate());
        userRepository.save(user);

        return user;
    }
}