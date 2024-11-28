package TheNaeunEconomy.account.admin.service.request;

import lombok.Getter;

@Getter
public class LoginAdminRequest {
    private String adminCode;
    private String password;

    public LoginAdminRequest() {
    }

    public LoginAdminRequest(String adminCode, String password) {
        this.adminCode = adminCode;
        this.password = password;
    }
}
