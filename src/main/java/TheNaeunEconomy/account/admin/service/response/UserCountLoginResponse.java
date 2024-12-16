package TheNaeunEconomy.account.admin.service.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@NoArgsConstructor
public class UserCountLoginResponse {
    private Long count;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    public UserCountLoginResponse(Long count, LocalDate date) {
        this.count = count;
        this.date = date;
    }
}
