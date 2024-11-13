package TheNaeunEconomy.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UpdateUserRequest {
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하로 입력해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotNull(message = "성별은 필수 항목입니다.")
    private String gender;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    private LocalDate birthDate;

    private LocalDate updateDate = LocalDate.now();
}
