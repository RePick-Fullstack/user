package TheNaeunEconomy.user.user.service.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
    private String email;
    private String password;

    public LoginUserRequest() {
    }

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}