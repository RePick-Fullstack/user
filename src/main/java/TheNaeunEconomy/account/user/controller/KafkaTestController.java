package TheNaeunEconomy.account.user.controller;

import TheNaeunEconomy.account.user.Dto.IsBilling;
import TheNaeunEconomy.account.user.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/kafka")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaService kafkaService;

    @GetMapping("/test")
    public String test() {
        kafkaService.sendMessage(new IsBilling(330483L, false));
        return "send message to kafka";
    }
}