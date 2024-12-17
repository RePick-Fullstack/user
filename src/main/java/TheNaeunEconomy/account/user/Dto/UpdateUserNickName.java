package TheNaeunEconomy.account.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserNickName {
    private Long userId;
    private String nickName;
}