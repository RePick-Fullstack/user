package TheNaeunEconomy.account.user.service.response;

import TheNaeunEconomy.account.user.domain.Gender;
import TheNaeunEconomy.account.user.domain.User;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserMyPageResponse {
    private final String name;
    private final String nickname;
    private final LocalDate birthDay;
    private final Gender gender;
    private final String email;
    private final String password;


    public UserMyPageResponse(User user) {
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.birthDay = user.getBirthDate();
        this.password = user.getPassword();
    }
}