package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.Dto.IsBilling;
import TheNaeunEconomy.account.user.Dto.UpdateUserNickName;

public interface KafkaService {
    void sendMessage(IsBilling isBilling);

    void userNickNameUpdate(UpdateUserNickName updateUserNickName);
}
