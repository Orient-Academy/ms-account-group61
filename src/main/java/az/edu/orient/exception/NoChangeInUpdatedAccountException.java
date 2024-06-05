package az.edu.orient.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NoChangeInUpdatedAccountException extends AccountException{
    public NoChangeInUpdatedAccountException(String message){
       super(message, HttpStatus.CONFLICT);
    }
}
