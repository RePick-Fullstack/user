package TheNaeunEconomy.user.request;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String gender;
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
