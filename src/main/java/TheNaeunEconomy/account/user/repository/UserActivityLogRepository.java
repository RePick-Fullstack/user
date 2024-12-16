package TheNaeunEconomy.account.user.repository;

import TheNaeunEconomy.account.admin.service.response.UserCountLoginResponse;
import TheNaeunEconomy.account.user.domain.UserActivityLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> {
    Optional<UserActivityLog> findByUserIdAndActivityDate(Long userId, LocalDate activityDate);

    @Query("SELECT new TheNaeunEconomy.account.admin.service.response.UserCountLoginResponse(COUNT(DISTINCT u.userId), :date) " +
            "FROM UserActivityLog u WHERE u.activityDate = :date")
    UserCountLoginResponse findDailyActiveUsers(@Param("date") LocalDate date);

    @Query("SELECT new TheNaeunEconomy.account.admin.service.response.UserCountLoginResponse(COUNT(DISTINCT u.userId), u.activityDate) " +
            "FROM UserActivityLog u " +
            "WHERE u.activityDate BETWEEN :startDate AND :endDate " +
            "GROUP BY u.activityDate " +
            "ORDER BY u.activityDate ASC")
    List<UserCountLoginResponse> findWeeklyActiveUsers(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}