package TheNaeunEconomy.user.service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    private String name;

    @Pattern(regexp = "^[가-힣\\s]+$", message = "닉네임은 한글과 띄어쓰기만 입력 가능합니다.")
    private String nickname;

    @Pattern(regexp = "^(M|F|기타)$", message = "성별은 M, F, 기타만 입력 가능합니다.")
    private String gender;

    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    private LocalDate updateDate = LocalDate.now();
}
