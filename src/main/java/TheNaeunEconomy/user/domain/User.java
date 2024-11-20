package TheNaeunEconomy.user.domain;


import TheNaeunEconomy.user.service.request.AddUserRequest;
import TheNaeunEconomy.user.service.request.KakaoAccountInfo;
import TheNaeunEconomy.user.service.request.UpdateUserRequest;
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

    @Column(name = "is_billing")
    private Boolean isBilling;


    public User(AddUserRequest request) {
        this.email = request.getEmail();
        this.password = bCryptPasswordEncoder.encode(request.getPassword());
        this.name = request.getName();
        this.nickname = request.getNickname().isEmpty() ? NicknameGenerator.generate() : request.getNickname();
        this.gender = request.getGender();
        this.birthDate = request.getBirthDate();
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.isBilling = false;
    }

    public User(KakaoAccountInfo kakaoAccountInfo) {
        this.email = kakaoAccountInfo.getEmail();
        this.password = bCryptPasswordEncoder.encode("password123!");
        this.name = kakaoAccountInfo.getName();
        this.nickname = NicknameGenerator.generate();
        this.gender = kakaoAccountInfo.getGender();
        this.birthDate = kakaoAccountInfo.getBirthDate();
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.isBilling = false;
    }


    public void updateUserDetails(UpdateUserRequest request) {
        if (request.getEmail() != null) {
            this.email = request.getEmail();
        }
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