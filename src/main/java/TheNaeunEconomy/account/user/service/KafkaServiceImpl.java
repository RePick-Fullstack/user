package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.user.Dto.IsBilling;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<String, IsBilling> kafkaTemplate;
    private final UserServiceImpl userService;

    @Override
    public void sendMessage(IsBilling isBilling) {
        kafkaTemplate.send("tosspayments", isBilling);
        System.out.println("Producer Sent : " + isBilling);
    }

    @KafkaListener(id = "user", topics = "tosspayments")
    public void listen(IsBilling isBilling) {
        System.out.println("Consumer IsBilling : " + isBilling);
        //서비스 생성
        userService.updateUserIsBilling(isBilling);
        //내용은 Dto/IsBilling  확인
    }
}
