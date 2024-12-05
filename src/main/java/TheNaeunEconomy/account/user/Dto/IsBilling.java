package TheNaeunEconomy.account.user.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IsBilling {
    Long userId;
    boolean isBilling;

    public boolean getIsBilling() {
        return isBilling;
    }
}
