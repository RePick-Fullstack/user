package TheNaeunEconomy.user.service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
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
