package az.edu.orient.dto;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.constant.ActiveStatusConstant;
import az.edu.orient.constant.CurrencyConstant;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AccountDto implements Cloneable{
    private Integer id;
    private Integer iban;
    @Enumerated(EnumType.STRING)
    private AccountTypeConstant accountType;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private CurrencyConstant currency;
    private Double balance;
    private Date openingDate;
    private Date closingDate;
    private ActiveStatusConstant activeStatus;

    @Override
    public AccountDto clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (AccountDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
