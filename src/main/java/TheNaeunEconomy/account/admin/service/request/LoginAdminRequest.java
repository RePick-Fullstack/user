package TheNaeunEconomy.account.admin.service.request;

import lombok.Getter;

@Getter
public class LoginAdminRequest {
    private Long adminCode;
    private String password;

    public LoginAdminRequest() {
    }

    public LoginAdminRequest(Long adminCode, String password) {
        this.adminCode = adminCode;
        this.password = password;
    }
}
