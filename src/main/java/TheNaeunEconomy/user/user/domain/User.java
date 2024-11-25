package TheNaeunEconomy.user.user.domain;


import TheNaeunEconomy.user.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.kakao_api.service.request.KakaoAccountInfo;
import TheNaeunEconomy.user.user.service.request.UpdateUserRequest;
import TheNaeunEconomy.user.util.NicknameGenerator;
import jakarta.persistence.*;

import java.time.LocalDate;
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
        this.password = bCryptPasswordEncoder.encode(kakaoAccountInfo.getEmail() + kakaoAccountInfo.getBirthDate());
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