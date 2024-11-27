package TheNaeunEconomy.account.admin.service.request;

import TheNaeunEconomy.account.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateAdminRequest {


    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    @Size(max = 5)
    private String name;

    private Role role;

    public UpdateAdminRequest(String name, Role role) {
        this.name = name;
        this.role = role;
    }
}
