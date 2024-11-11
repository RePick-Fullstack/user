package TheNaeunEconomy.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

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
    private Date birthDate;

    @Override
    public String toString() {
        return "AddUserRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}