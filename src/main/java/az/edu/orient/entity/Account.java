package az.edu.orient.entity;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.constant.CurrencyConstant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String iban;
    @Enumerated(EnumType.STRING)
    private AccountTypeConstant accountType;
    private String accountNumber;
    private CurrencyConstant currency;
    private Double balance;
    private Date openingDate;
    private Date closingDate;
    private byte activeStatus;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return activeStatus == account.activeStatus && Objects.equals(id, account.id) && Objects.equals(iban, account.iban) && accountType == account.accountType && Objects.equals(accountNumber, account.accountNumber) && currency == account.currency && Objects.equals(balance, account.balance) && Objects.equals(openingDate, account.openingDate) && Objects.equals(closingDate, account.closingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iban, accountType, accountNumber, currency, balance, openingDate, closingDate, activeStatus);
    }
}
