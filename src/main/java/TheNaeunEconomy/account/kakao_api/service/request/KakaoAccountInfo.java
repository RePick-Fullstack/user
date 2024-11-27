package TheNaeunEconomy.account.kakao_api.service.request;


import TheNaeunEconomy.account.user.domain.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class KakaoAccountInfo {
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 항목입니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    @Size(max = 5)
    private String name;

    @Size(max = 10)
    @Pattern(regexp = "^[가-힣\\s]*$", message = "닉네임은 한글과 띄어쓰기만 입력 가능합니다.")
    private String nickname;

    @NotNull(message = "성별은 필수 항목입니다.")
    private Gender gender;

    @NotNull(message = "생년월일은 필수 항목입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    public KakaoAccountInfo() {
    }

    public KakaoAccountInfo(String email, String name, String nickname, Gender gender, LocalDate birthDate) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.gender = gender;
        this.birthDate = birthDate;
    }
}