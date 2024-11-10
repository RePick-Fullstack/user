package TheNaeunEconomy.user.domain;


import jakarta.persistence.*;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false)
    private Long id;

    @NotNull
    @Column(name = "email", unique = true)
    private String email;
    @NotNull
    @Column(name = "password")
    private String password;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "nickname")
    private String nickname;
    @NotNull
    @Column(name = "gender")
    private String gender;
    @NotNull
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

    @Builder
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @PrePersist
    public void prePersist() {
        Date currentDate = new Date();
        this.createDate = currentDate;
        this.updateDate = currentDate;
    }

    @Override
    public String getUsername() {
        return email;
    }
}