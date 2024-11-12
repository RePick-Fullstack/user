package TheNaeunEconomy.user.domain;


import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

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
    private Date birthDate;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "delete_date")
    private Date deleteDate;
    @Column(name = "is_billing")
    private Boolean isBilling;
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public User(String email, String password, String name, String nickname, String gender, Date birthDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.isBilling = false;
        this.isDeleted = false;
    }

    @PrePersist
    public void prePersist() {
        Date currentDate = new Date();
        this.createDate = currentDate;
        this.updateDate = currentDate;
    }
}