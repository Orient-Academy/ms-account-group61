package az.edu.orient.service;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.constant.ActiveStatusConstant;
import az.edu.orient.constant.CurrencyConstant;
import az.edu.orient.constant.ExceptionMessageConstant;
import az.edu.orient.dto.AccountDto;
import az.edu.orient.entity.Account;
import az.edu.orient.exception.AccountNotFoundException;
import az.edu.orient.exception.InsufficientBalanceException;
import az.edu.orient.exception.NoChangeInUpdatedAccountException;
import az.edu.orient.mapper.AccountMapper;
import az.edu.orient.repository.AccountRepository;
import az.edu.orient.utility.IbanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final IbanUtil ibanUtil;

    public List<AccountDto> getAllAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper.INSTANCE::mapAccount)
                .toList();
    }

    public AccountDto getAccountById(int id) {
        Account account = getById(id);
        return AccountMapper.INSTANCE.mapAccount(account);
    }

    public List<AccountDto> getAccountsByAccountType(AccountTypeConstant accountType) {
        List<Account> accountList = accountRepository.findAllByAccountType(accountType);
        return accountList.stream().map(AccountMapper.INSTANCE::mapAccount).toList();
    }

    public AccountDto createAccount(AccountDto accountDto){
        Account account = AccountMapper.INSTANCE.mapAccount(accountDto);
        Account createdAccount = accountRepository.save(account);
        return AccountMapper.INSTANCE.mapAccount(createdAccount);
    }

    public AccountDto createAccountInternal(AccountDto accountDto){
        Account account = new Account();
        account.setUserId(accountDto.getUserId());
        account.setAccountNumber(UUID.randomUUID().toString());
        //account.setIban(ibanUtil.generateIBAN("AZE", "123", String.valueOf(account.getAccountNumber())));
        account.setIban(UUID.randomUUID().toString());
        account.setBalance(0.0d);
        account.setCurrency(CurrencyConstant.AZN);
        account.setOpeningDate(new Date());
        account.setClosingDate(null);
        account.setActiveStatus(ActiveStatusConstant.ACTIVE.getValue());
        Account createdAccount = accountRepository.save(account);
        return AccountMapper.INSTANCE.mapAccount(createdAccount);
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        Account currentAccount = getById(accountDto.getId());
        Account updatedAccount = AccountMapper.INSTANCE.mapAccount(accountDto);
        if (currentAccount.equals(updatedAccount)){
            throw new NoChangeInUpdatedAccountException(ExceptionMessageConstant.NO_CHANGE_IN_UPDATED_ACCOUNT.getMessage());
        }
        Account savedAccount = accountRepository.save(updatedAccount);
        return AccountMapper.INSTANCE.mapAccount(savedAccount);
    }

    public void deleteAccount(int id) {
        getById(id);
        accountRepository.deleteById(id);
    }

    public AccountDto addIncome(Integer accountId, Double amount) {
        Account account = getById(accountId);
        account.setBalance(account.getBalance() + amount);
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.INSTANCE.mapAccount(updatedAccount);
    }

    public AccountDto addExpenditure(Integer accountId, Double amount) {
        Account account = getById(accountId);
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(ExceptionMessageConstant.INSUFFICIENT_BALANCE.getMessage());
        }
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.INSTANCE.mapAccount(updatedAccount);
    }

    private Account getById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage()));
    }
}
