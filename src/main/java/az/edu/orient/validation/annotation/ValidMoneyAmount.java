package az.edu.orient.validation.annotation;

import az.edu.orient.validation.validator.MoneyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MoneyValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMoneyAmount {
    String message() default "Invalid money amount";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
