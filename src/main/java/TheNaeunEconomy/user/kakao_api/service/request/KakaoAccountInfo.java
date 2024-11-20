package TheNaeunEconomy.user.kakao_api.service.request;

import TheNaeunEconomy.user.user.domain.Gender;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class KakaoAccountInfo {
    private final String email;
    private final String name;
    private Gender gender;
    private final LocalDate birthDate;

    public KakaoAccountInfo(String email, String name, String birthYear, String birthDay, String gender) {
        this.email = email;
        this.name = name;
        String formattedDate = birthYear + "-" + birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);

        this.birthDate = LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(birthDate);
        if (gender.equals("male"))
        {
            this.gender = Gender.MALE;
        }
        if (gender.equals("female"))
        {
            this.gender = Gender.FEMALE;
        }
    }


}
