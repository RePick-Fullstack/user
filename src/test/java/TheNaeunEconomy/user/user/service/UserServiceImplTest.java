package TheNaeunEconomy.user.user.service;

import TheNaeunEconomy.account.domain.Gender;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.UserServiceImpl;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("유저를 저장한 후 이메일이 제대로 저장이 되어있는지 확인한다.")
    public void saveUserTest() {
        AddUserRequest addUserRequest = new AddUserRequest("ckdghks5565@gmail.com", "password123", "최창환", "근면한 복어",
                Gender.MALE, LocalDate.of(2001, 6, 5));

        User user = userService.saveUser(addUserRequest);

        Assertions.assertThat(user.getEmail()).isEqualTo(addUserRequest.getEmail());
    }
}