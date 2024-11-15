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

    @Scheduled(cron = "0 0 0 1 * ?")
    public void performRealDelete() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        List<User> usersToDelete = userRepository.findByIsDeletedTrueAndDeleteDateBefore(threeMonthsAgo);

        for (User user : usersToDelete) {
            userRepository.delete(user); // 3개월 후 실제 데이터베이스에서 삭제
            System.out.println("User " + user.getEmail() + " has been deleted.");
        }
    }
}