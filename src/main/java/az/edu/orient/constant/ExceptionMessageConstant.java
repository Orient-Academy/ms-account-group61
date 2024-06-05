package az.edu.orient.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessageConstant {
    ACCOUNT_NOT_FOUND_MESSAGE("Account not found!"),
    INSUFFICIENT_BALANCE("Insufficient balance!"),
    NO_CHANGE_IN_UPDATED_ACCOUNT("There is not any change in updated account!"),
    INCORRECT_ACTIVE_STATUS_CONSTANT_VALUE("Incorrect active status constant!"),;

    private final String message;
}
