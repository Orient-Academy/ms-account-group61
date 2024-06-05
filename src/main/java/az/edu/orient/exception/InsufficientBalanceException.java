package az.edu.orient.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InsufficientBalanceException extends AccountException{
    public InsufficientBalanceException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
