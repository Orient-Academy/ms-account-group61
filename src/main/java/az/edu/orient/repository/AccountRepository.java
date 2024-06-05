package az.edu.orient.repository;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAllByAccountType(AccountTypeConstant accountType);
}
