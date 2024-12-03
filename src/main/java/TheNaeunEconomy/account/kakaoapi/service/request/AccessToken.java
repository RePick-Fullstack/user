package TheNaeunEconomy.account.kakaoapi.service.request;

import TheNaeunEconomy.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessToken {
    private Token accessToken;
}
