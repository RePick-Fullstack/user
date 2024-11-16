package TheNaeunEconomy.user.service.reponse;

import TheNaeunEconomy.user.config.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenResponse {
    private Token accessToken;
}
