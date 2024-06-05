package az.edu.orient.validation.validator;

import az.edu.orient.exception.InvalidMoneyAmountException;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoneyValidatorTest {

    @InjectMocks
    MoneyValidator moneyValidator;

    @Mock
    ConstraintValidatorContext context;

    @Test
    void initialize() {
    }

    @Test
    void isValidGivenMoneyAmountIsNullExpectThrowInvalidMoneyAmountException() {
        // setup
        Double amount = null;
        String message = "Money amount must be non-null value!";
        // when & then
        InvalidMoneyAmountException ex = assertThrows(InvalidMoneyAmountException.class,
                () -> moneyValidator.isValid(amount, context));
        assertEquals(message, ex.getMessage());
    }

    @Test
    void isValidGivenMoneyAmountIsZeroExpectThrowInvalidMoneyAmountException() {
        // setup
        Double amount = 0.0;
        String message = "Money amount must be greater than 0!";
        // when & then
        InvalidMoneyAmountException ex = assertThrows(
                InvalidMoneyAmountException.class,
                () -> moneyValidator.isValid(amount, context)
        );
        assertEquals(message, ex.getMessage());
    }

    @Test
    void isValidGivenMoneyAmountIsNegativeExpectThrowInvalidMoneyAmountException() {
        // setup
        Double amount = -1.0;
        String message = "Money amount must be greater than 0!";
        // when & then
        InvalidMoneyAmountException ex = assertThrows(
                InvalidMoneyAmountException.class,
                () -> moneyValidator.isValid(amount, context)
        );
        assertEquals(message, ex.getMessage());
    }

    @Test
    void isValidGivenMoneyAmountIsPositiveExpectReturnTrue() {
        // setup
        Double amount = 1.0;
        // when
        boolean result = moneyValidator.isValid(amount, context);
        //then
        assertTrue(result);
    }
}