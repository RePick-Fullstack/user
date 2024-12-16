package TheNaeunEconomy.account.admin.service.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCountLoginResponse {
    private Long count;

    public UserCountLoginResponse(Long count) {
        this.count = count;
    }
}
