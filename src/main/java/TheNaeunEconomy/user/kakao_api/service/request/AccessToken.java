package TheNaeunEconomy.user.kakao_api.service.request;

import TheNaeunEconomy.user.jwt.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessToken {
    private Token accessToken;
}
