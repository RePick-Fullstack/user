package TheNaeunEconomy.account.user.repository;

import TheNaeunEconomy.account.user.domain.UserActivityLog;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
    Optional<UserActivityLog> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);
}
