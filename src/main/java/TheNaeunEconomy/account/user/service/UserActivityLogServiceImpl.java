package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.admin.service.response.UserCountLoginResponse;
import TheNaeunEconomy.account.user.repository.UserActivityLogRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityLogServiceImpl {

    private final UserActivityLogRepository userActivityLogRepository;

    public UserCountLoginResponse getDAU(LocalDate date) {
        return userActivityLogRepository.findDailyActiveUsers(date);
    }

    public Long getWAU(LocalDate date) {
        LocalDate startDate = date.minusDays(6);
        return userActivityLogRepository.findWeeklyActiveUsers(startDate, date);
    }
}
