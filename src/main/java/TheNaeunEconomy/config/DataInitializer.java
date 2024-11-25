package TheNaeunEconomy.config;

import TheNaeunEconomy.account.user.repository.UserRepository;
import TheNaeunEconomy.account.domain.Gender;
import TheNaeunEconomy.account.domain.User;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.util.NicknameGenerator;
import jakarta.annotation.PreDestroy;
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

    private final UserRepository userRepository;

    private static final List<String> NAMES = List.of(
            "김민준", "이서연", "박지민", "최준호", "정다은", "조유진", "장하늘", "강지수", "오은영", "임서진"
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            int year = 1990 + random.nextInt(11);
            int month = 1 + random.nextInt(12);
            int day = 1 + random.nextInt(28);
            Gender gender = i % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            AddUserRequest request = new AddUserRequest("example" + i + "@gmail.com", "password123",
                    NAMES.get(random.nextInt(NAMES.size())), NicknameGenerator.generate(), gender,
                    LocalDate.of(year, month, day));

            if (userRepository.findByEmail("example" + i + "@gmail.com").isEmpty()) {
                saveUser(request);
            }
        }

        log.info("100개의 더미 데이터가 성공적으로 삽입되었습니다.");
    }

    private void saveUser(AddUserRequest request) {
        User user = new User(request);
        userRepository.save(user);
    }

    @PreDestroy
    public void cleanUp() {
        userRepository.deleteAll();
        log.info("애플리케이션 종료 시 모든 더미 데이터가 삭제되었습니다.");
    }
}