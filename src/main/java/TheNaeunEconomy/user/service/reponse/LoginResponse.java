package TheNaeunEconomy.user.service.reponse;


import TheNaeunEconomy.user.config.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Token accessToken;
    private Token refreshToken;
}
