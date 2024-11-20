package TheNaeunEconomy.user.user.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import TheNaeunEconomy.user.user.domain.Gender;
import TheNaeunEconomy.user.user.repository.UserRepository;
import TheNaeunEconomy.user.jwt.RefreshTokenRepository;
import TheNaeunEconomy.user.jwt.TokenProvider;
import TheNaeunEconomy.user.user.service.UserServiceImpl;
import TheNaeunEconomy.user.user.service.request.AddUserRequest;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저를 AddUserRequest로 받아 저장한다.")
    void saveUser() {
        AddUserRequest addUserRequest = new AddUserRequest();

        addUserRequest.setEmail("ckdghks5565@gmail.com");
        addUserRequest.setPassword("password123!");
        addUserRequest.setName("최창환");
        addUserRequest.setNickname("근면한 복어");
        addUserRequest.setGender(Gender.MALE);
        addUserRequest.setBirthDate(LocalDate.now());

        HttpStatus status = userService.saveUser(addUserRequest);

        assertEquals(HttpStatus.OK, status);
    }
}