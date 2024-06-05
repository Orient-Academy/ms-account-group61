package az.edu.orient.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
