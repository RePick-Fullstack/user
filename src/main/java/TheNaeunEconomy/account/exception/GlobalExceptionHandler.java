package TheNaeunEconomy.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<SimpleErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>(
                new SimpleErrorResponse("USER_DELETED", ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SimpleErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new SimpleErrorResponse("INVALID_ARGUMENT", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
