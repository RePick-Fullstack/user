package TheNaeunEconomy.account.kakao_api.service.request;

import TheNaeunEconomy.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessToken {
    private Token accessToken;
}
