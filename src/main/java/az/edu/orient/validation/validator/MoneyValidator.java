package az.edu.orient.validation.validator;

import az.edu.orient.exception.InvalidMoneyAmountException;
import az.edu.orient.validation.annotation.ValidMoneyAmount;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MoneyValidator implements ConstraintValidator<ValidMoneyAmount, Double> {

    @Override
    public void initialize(ValidMoneyAmount constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Double moneyAmount, ConstraintValidatorContext context) {
        if (moneyAmount == null)  {
            throw new InvalidMoneyAmountException("Money amount must be non-null value!");
        }
        else if (moneyAmount <= 0) {
            throw new InvalidMoneyAmountException("Money amount must be greater than 0!");
        }
        return true;
    }
}
