package TheNaeunEconomy.account.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleErrorResponse {
    private String errorCode;
    private String message;

    public SimpleErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
