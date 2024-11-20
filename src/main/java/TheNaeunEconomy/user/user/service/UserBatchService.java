package TheNaeunEconomy.user.user.service;

import TheNaeunEconomy.user.user.repository.UserRepository;
import TheNaeunEconomy.user.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserBatchService {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Scheduled(cron = "0 0 * * * ?")
    public void cleanUpExpiredTokens() {
        userService.deleteExpiredTokens();
        System.out.println("Expired tokens cleaned up.");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void performRealDelete() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        List<User> usersToDelete = userRepository.findByDeleteDateBeforeAndDeleteDateIsNotNull(threeMonthsAgo);

        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }
    }
}
