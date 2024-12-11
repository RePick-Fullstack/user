package TheNaeunEconomy.account.user.Dto;
import lombok.Data;


@Data
public class UpdateUserNickName {
    private Long userId;
    private String nickName;
}