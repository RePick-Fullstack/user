package TheNaeunEconomy.user.service;

import TheNaeunEconomy.user.Repository.UserRepository;
import TheNaeunEconomy.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserBatchService {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void performRealDelete() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        List<User> usersToDelete = userRepository.findByDeleteDateBeforeAndDeleteDateIsNotNull(threeMonthsAgo);

        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }
    }
}
