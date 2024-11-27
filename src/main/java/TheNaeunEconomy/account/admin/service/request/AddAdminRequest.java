package TheNaeunEconomy.account.admin.service.request;

import TheNaeunEconomy.account.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AddAdminRequest {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    @Size(min = 8, max = 8, message = "아이디는 8자리이어야 합니다.")
    private String adminCode;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 18, message = "비밀번호는 8자 이상, 18자 이하입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    @Size(max = 5)
    private String name;

    private Role role;

    public AddAdminRequest(String adminCode, String password, String name) {
        this.adminCode = adminCode;
        this.password = password;
        this.name = name;
        this.role = Role.ADMIN;
    }

    public AddAdminRequest(String adminCode, String password, String name, Role role) {
        this.adminCode = adminCode;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
