package TheNaeunEconomy.account.admin.domain;

import TheNaeunEconomy.account.admin.service.request.AddAdminRequest;
import TheNaeunEconomy.account.admin.service.request.UpdateAdminRequest;
import TheNaeunEconomy.account.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Table(name = "admins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Admin {
    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "admin_code", unique = true)
    private String adminCode;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "role")
    private Role role;

    public Admin(AddAdminRequest addAdminRequest) {
        this.adminCode = addAdminRequest.getAdminCode();
        this.name = addAdminRequest.getName();
        this.password = bCryptPasswordEncoder.encode(addAdminRequest.getPassword());
        this.role = addAdminRequest.getRole();
    }

    public void updateUserDetails(UpdateAdminRequest request) {
        if (request.getAdminCode() != null) {
            this.adminCode = request.getAdminCode();
        }
        if (request.getName() != null) {
            name = request.getName();
        }
        if (request.getRole() != null) {
            role = request.getRole();
        }
    }
}