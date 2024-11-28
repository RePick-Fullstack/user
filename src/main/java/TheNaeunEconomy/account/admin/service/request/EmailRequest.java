package TheNaeunEconomy.account.admin.service.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {
    @NotBlank
    private String email;
}
