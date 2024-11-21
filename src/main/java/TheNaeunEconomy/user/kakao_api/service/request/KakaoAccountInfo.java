package TheNaeunEconomy.user.kakao_api.service.request;


import TheNaeunEconomy.user.user.domain.Gender;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class KakaoAccountInfo {
    private String email;
    private String name;
    private String nickname;
    private Gender gender;
    private LocalDate birthDate;
}