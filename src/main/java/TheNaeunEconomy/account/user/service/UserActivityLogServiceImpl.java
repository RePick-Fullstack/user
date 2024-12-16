package TheNaeunEconomy.account.user.service;

import TheNaeunEconomy.account.admin.service.response.UserCountLoginResponse;
import TheNaeunEconomy.account.user.repository.UserActivityLogRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityLogServiceImpl {

    private final UserActivityLogRepository userActivityLogRepository;

    public UserCountLoginResponse getDAU(LocalDate date) {
        return userActivityLogRepository.findDailyActiveUsers(date);
    }

    public List<UserCountLoginResponse> getWAU(LocalDate date) {
        LocalDate startDate = date.minusDays(6);
        return userActivityLogRepository.findWeeklyActiveUsers(startDate, date);
    }

    public List<UserCountLoginResponse> getMAU(int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        return userActivityLogRepository.findMonthlyActiveUsersDaily(startOfMonth, endOfMonth);
    }
}