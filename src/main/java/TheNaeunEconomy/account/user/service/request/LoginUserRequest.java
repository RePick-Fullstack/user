package TheNaeunEconomy.account.user.service.request;

import lombok.Getter;

@Getter
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