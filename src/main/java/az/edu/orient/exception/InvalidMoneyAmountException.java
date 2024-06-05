package az.edu.orient.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidMoneyAmountException extends AccountException{
    public InvalidMoneyAmountException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
