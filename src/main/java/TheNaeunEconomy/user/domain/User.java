package TheNaeunEconomy.user.domain;


import TheNaeunEconomy.user.service.request.UpdateUserRequest;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "gender")
    private String gender;
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
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public User(String email, String password, String name, String nickname, String gender, LocalDate birthDate) {
        this.uuid = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.createDate = LocalDate.now();
        this.updateDate = LocalDate.now();
        this.isBilling = false;
        this.isDeleted = false;
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