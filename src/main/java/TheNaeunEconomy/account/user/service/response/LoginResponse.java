package TheNaeunEconomy.account.user.service.response;


import TheNaeunEconomy.jwt.Token;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Token accessToken;
    private Token refreshToken;

    public LoginResponse(Token accessToken, Token refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}