package TheNaeunEconomy.account.user.domain;


import static TheNaeunEconomy.account.user.domain.User.RandomDateGenerator.generateRandomDate;

import TheNaeunEconomy.account.domain.Role;
import TheNaeunEconomy.account.naverapi.service.request.NaverAccountInfo;
import TheNaeunEconomy.account.user.service.request.AddDevUserRequest;
import TheNaeunEconomy.account.user.service.request.AddUserRequest;
import TheNaeunEconomy.account.kakaoapi.service.request.KakaoAccountInfo;
import TheNaeunEconomy.account.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.util.NicknameGenerator;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User {

    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "delete_date")
    private LocalDate deleteDate;

    @Column(name = "role")
    private Role role;

    @Column(name = "is_billing")
    private Boolean isBilling;

    public static class RandomDateGenerator {

        public static LocalDate generateRandomDate() {
            int year = ThreadLocalRandom.current().nextInt(2023, 2025); // 2023 ~ 2024 중 랜덤

            LocalDate startDate = LocalDate.of(year, 1, 1); // 1월 1일
            LocalDate endDate = LocalDate.of(year, 12, 31); // 12월 31일

            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);

            return startDate.plusDays(randomDays);
        }
    }

    public User(AddDevUserRequest addUserRequest) {
        this.email = addUserRequest.getEmail();
        this.password = bCryptPasswordEncoder.encode(addUserRequest.getPassword());
        this.name = addUserRequest.getName();
        this.nickname =
                addUserRequest.getNickname().isEmpty() ? NicknameGenerator.generate() : addUserRequest.getNickname();
        this.gender = addUserRequest.getGender();
        this.birthDate = addUserRequest.getBirthDate();
        this.createDate = generateRandomDate();
        this.updateDate = LocalDate.now();
        this.role = Role.USER;
        this.isBilling = false;
    }

    public User(AddUserRequest addUserRequest) {
        this.email = addUserRequest.getEmail();
        this.password = bCryptPasswordEncoder.encode(addUserRequest.getPassword());
        this.name = addUserRequest.getName();
        this.nickname =
                addUserRequest.getNickname().isEmpty() ? NicknameGenerator.generate() : addUserRequest.getNickname();
        this.gender = addUserRequest.getGender();
        this.birthDate = addUserRequest.getBirthDate();
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.role = Role.USER;
        this.isBilling = false;
    }

    public User(KakaoAccountInfo kakaoAccountInfo) {
        this.email = kakaoAccountInfo.getEmail();
        this.password = bCryptPasswordEncoder.encode(kakaoAccountInfo.getEmail());
        this.name = kakaoAccountInfo.getName();
        this.nickname = kakaoAccountInfo.getNickname().isEmpty() ? NicknameGenerator.generate()
                : kakaoAccountInfo.getNickname();
        this.gender = kakaoAccountInfo.getGender();
        this.birthDate = kakaoAccountInfo.getBirthDate();
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.role = Role.USER;
        this.isBilling = false;
    }

    public User(NaverAccountInfo naverAccountInfo) {
        this.email = naverAccountInfo.getEmail();
        this.password = bCryptPasswordEncoder.encode(naverAccountInfo.getEmail());
        this.name = naverAccountInfo.getName();
        this.nickname = naverAccountInfo.getNickname().isEmpty() ? NicknameGenerator.generate()
                : naverAccountInfo.getNickname();
        this.gender = naverAccountInfo.getGender();
        this.birthDate = naverAccountInfo.getBirthDate();
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.role = Role.USER;
        this.isBilling = false;
    }

    public void updateUserDetails(UpdateUserRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (request.getGender() != null) {
            this.gender = request.getGender();
        }
        if (request.getBirthDate() != null) {
            this.birthDate = request.getBirthDate();
        }
        this.updateDate = LocalDate.now();
    }
}