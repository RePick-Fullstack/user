package TheNaeunEconomy.account.user.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsBilling {
    Long userId;
    boolean isBilling;
}
