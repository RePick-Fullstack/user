package TheNaeunEconomy.config;

import TheNaeunEconomy.account.admin.domain.Admin;
import TheNaeunEconomy.account.admin.repository.AdminRepository;
import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.domain.Role;
import TheNaeunEconomy.account.user.domain.UserActivityLog;
import TheNaeunEconomy.account.user.repository.UserActivityLogRepository;
import TheNaeunEconomy.account.user.repository.UserRepository;
import TheNaeunEconomy.account.user.domain.Gender;
import TheNaeunEconomy.account.user.domain.User;
import TheNaeunEconomy.account.user.service.request.AddDevUserRequest;
import TheNaeunEconomy.jwt.RefreshTokenRepository;
import TheNaeunEconomy.util.NicknameGenerator;
import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final UserActivityLogRepository userActivityLogRepository;

    private static final List<String> NAMES = List.of("김민준", "이서연", "박지민", "최준호", "정다은", "조유진", "장하늘", "강지수", "오은영",
            "임서진");

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 관리자 생성
        AddAdminRequest addAdminRequest = new AddAdminRequest("cch5565", "password123!", "최창환", Role.SUPER_ADMIN);
        saveAdmin(addAdminRequest);

        // 사용자 생성
        Random random = new Random();
        for (int i = 1; i <= 1000; i++) {
            int year = 1970 + random.nextInt(15); // 1970 ~ 1980
            int month = 1 + random.nextInt(12); // 1 ~ 12
            int day = 1 + random.nextInt(28); // 1 ~ 28 (월별 일수 고려)
            Gender gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;

            AddDevUserRequest request = new AddDevUserRequest("example" + i + "@gmail.com", "password123",
                    NAMES.get(random.nextInt(NAMES.size())), NicknameGenerator.generate(), gender,
                    LocalDate.of(year, month, day));

            if (userRepository.findByEmail("example" + i + "@gmail.com").isEmpty()) {
                saveUser(request);
            }
        }
        log.info("1000개의 더미 사용자 데이터가 성공적으로 삽입되었습니다.");

        // 활동 로그 생성
        generateUserActivityLogs();
        log.info("지난 7일간의 사용자 활동 로그가 성공적으로 삽입되었습니다.");
    }

    private void saveUser(AddDevUserRequest request) {
        User user = new User(request);
        userRepository.save(user);
    }

    private void saveAdmin(AddAdminRequest request) {
        Admin admin = new Admin(request);
        adminRepository.save(admin);
    }

    private void generateUserActivityLogs() {
        // 활동 로그 생성 범위 설정 (오늘 기준으로 지난 6일 ~ 오늘)
        LocalDate today = LocalDate.now();
        List<LocalDate> activityDates = List.of(today.minusDays(6), today.minusDays(5), today.minusDays(4),
                today.minusDays(3), today.minusDays(2), today.minusDays(1), today);

        Random random = new Random();

        // 모든 사용자 가져오기 (이미 생성된 사용자)
        List<User> users = userRepository.findAll();

        // 각 사용자에 대해 지난 7일간 활동 로그 생성
        for (User user : users) {
            for (LocalDate activityDate : activityDates) {
                // 랜덤하게 활동 여부 결정 (예: 65% 확률로 활동)
                if (random.nextDouble() < 0.65) {
                    // 중복 확인
                    boolean exists = userActivityLogRepository.findByUserIdAndActivityDate(user.getId(), activityDate)
                            .isPresent();
                    if (!exists) {
                        // 랜덤한 시간 생성 (예: 오전 6시 ~ 오후 11시)
                        int hour = 6 + random.nextInt(18); // 6 ~ 23
                        int minute = random.nextInt(60);
                        int second = random.nextInt(60);
                        LocalDateTime createdAt = activityDate.atTime(hour, minute, second);

                        UserActivityLog log = new UserActivityLog();
                        log.setUserId(user.getId());
                        log.setActivityDate(activityDate);
                        log.setCreatedAt(createdAt);

                        userActivityLogRepository.save(log);
                    }
                }
            }
        }
    }

    @PreDestroy
    public void cleanUp() {
        refreshTokenRepository.deleteAll();
        userActivityLogRepository.deleteAll();
        userRepository.deleteAll();
        adminRepository.deleteAll();
        log.info("애플리케이션 종료 시 모든 더미 데이터가 삭제되었습니다.");
    }
}