package TheNaeunEconomy.account.admin.service.request;

import TheNaeunEconomy.account.domain.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateAdminRequest {

    @NotNull
    private Long adminId;

    private String adminCode;

    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    @Size(max = 5)
    private String name;

    private Role role;

    public UpdateAdminRequest() {
    }

    public UpdateAdminRequest(Long adminId, String adminCode, String name, Role role) {
        this.adminId = adminId;
        this.adminCode = adminCode;
        this.name = name;
        this.role = role;
    }
}
