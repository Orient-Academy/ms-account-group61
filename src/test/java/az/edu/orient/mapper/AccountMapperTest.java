package az.edu.orient.mapper;


import az.edu.orient.constant.ActiveStatusConstant;
import az.edu.orient.constant.ExceptionMessageConstant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    @Test
    void mapActiveStatusGivenCorrectValueReturnActiveStatusConstant() {
        // setup
        byte value = 1;
        // when
        ActiveStatusConstant constant = AccountMapper.INSTANCE.mapActiveStatus(value);
        // then
        assertNotNull(constant);
        assertEquals(ActiveStatusConstant.ACTIVE, constant);
    }

    @Test
    void mapActiveStatusGivenIncorrectValueExpectThrowIllegalArgumentException() {
        // setup
        byte value = -1;

        // when & then
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> AccountMapper.INSTANCE.mapActiveStatus(value));
        assertEquals(ex.getMessage(), ExceptionMessageConstant.INCORRECT_ACTIVE_STATUS_CONSTANT_VALUE.getMessage());
    }


}