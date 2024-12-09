package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.Dto.IsBilling;
import TheNaeunEconomy.account.user.Dto.UpdateUserNickName;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserServiceImpl userService;

    @Override
    public void sendMessage(IsBilling isBilling) {
        kafkaTemplate.send("tosspayments", isBilling);
    }

    @Override
    public void userNickNameUpdate(UpdateUserNickName updateUserNickName) {
        kafkaTemplate.send("updateusernickname", updateUserNickName);
    }

    @KafkaListener(id = "user-tosspayments", topics = "tosspayments")
    public void listen(IsBilling isBilling) {
        userService.updateUserIsBilling(isBilling);
    }

    @KafkaListener(id = "user-nickname", topics = "updateusernickname")
    public void listen(UpdateUserNickName updateUserNickName){
        System.out.println(updateUserNickName);
    }
}