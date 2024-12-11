package TheNaeunEconomy.account.user.service.response;

import TheNaeunEconomy.account.user.domain.User;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserMyPageResponse {
    private final String name;
    private final String nickname;
    private final LocalDate birthDay;
    private final String email;
    private final String password;


    public UserMyPageResponse(User user) {
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.birthDay = user.getBirthDate();
        this.password = user.getPassword();
    }
}