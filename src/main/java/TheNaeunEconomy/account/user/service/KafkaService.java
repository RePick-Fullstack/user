package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.Dto.IsBilling;

public interface KafkaService {
    void sendMessage(IsBilling isBilling);
}
