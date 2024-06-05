package az.edu.orient.service;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.constant.ActiveStatusConstant;
import az.edu.orient.constant.CurrencyConstant;
import az.edu.orient.constant.ExceptionMessageConstant;
import az.edu.orient.dto.AccountDto;
import az.edu.orient.entity.Account;
import az.edu.orient.exception.*;
import az.edu.orient.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Test
    void getAllAccountsExpectReturnAccountDtoList() {
        // setup
        List<Account> accountList = new ArrayList<>();
        Account account = Account.builder()
                .id(1)
                .accountNumber("323245464")
                .balance(42500.5)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.SAVINGS)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();
        accountList.add(account);
        Mockito.when(accountRepository.findAll()).thenReturn(accountList);
        // when
        List<AccountDto> accountDtoList = accountService.getAllAccounts();
        // then
        assertNotNull(accountDtoList);
        assertEquals(accountList.size(), accountDtoList.size());
    }

    @Test
    void getAccountByIdGivenIncorrectIdExpectThrowsAccountNotFoundException() {
        // setup
        int givenId = -1;
        Mockito.when(accountRepository.findById(givenId)).thenReturn(Optional.empty());
        // when
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(givenId));
        // then
        assertEquals(ex.getMessage(), ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage());
        assertEquals(ex.getHttpStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    void getAccountByIdGivenCorrectIdExpectReturnsAccountDto() {
        // setup
        int givenId = 1;
        Account account = Account.builder()
                .id(1)
                .accountNumber("323245464")
                .balance(42500.5)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.SAVINGS)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();
        Mockito.when(accountRepository.findById(givenId)).thenReturn(Optional.of(account));
        // when
        AccountDto accountDto = accountService.getAccountById(givenId);
        // then
        assertNotNull(accountDto);
        assertEquals(accountDto.getAccountNumber(), account.getAccountNumber());
    }

    @Test
    void getAccountsByAccountTypeGivenCorrectTypeExpectReturnsAccountDto() {
        // setup
        AccountTypeConstant accountType = AccountTypeConstant.SAVINGS;
        List<Account> accountList = new ArrayList<>();
        Account account = Account.builder()
                .id(1)
                .accountNumber("323245464")
                .balance(42500.5)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.SAVINGS)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();
        accountList.add(account);
        Mockito.when(accountRepository.findAllByAccountType(accountType)).thenReturn(accountList);
        // when
        List<AccountDto> accountDtoList = accountService.getAccountsByAccountType(accountType);
        // then
        assertNotNull(accountDtoList);
        assertEquals(accountDtoList.size(), accountList.size());
        assertEquals(accountDtoList.get(0).getAccountNumber(), accountList.get(0).getAccountNumber());
    }

    @Test
    void createAccountGivenAccountDtoExpectReturnAccountDto() {
        // setup
        AccountDto accountDto = AccountDto.builder()
                .id(1)
                .accountNumber("323245464")
                .balance(42500.5)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.SAVINGS)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE)
                .build();

        // Convert AccountDto to Account
        Account account = Account.builder()
                .id(accountDto.getId())
                .accountNumber(accountDto.getAccountNumber())
                .balance(accountDto.getBalance())
                .currency(accountDto.getCurrency())
                .accountType(accountDto.getAccountType())
                .openingDate(accountDto.getOpeningDate())
                .activeStatus(accountDto.getActiveStatus().getValue())
                .build();

        Mockito.when(accountRepository.save(account)).thenReturn(account);

        // when
        AccountDto createdAccountDto = accountService.createAccount(accountDto);

        // then
        assertNotNull(createdAccountDto);
        assertEquals(createdAccountDto.getAccountNumber(), accountDto.getAccountNumber());
        assertEquals(createdAccountDto.getAccountNumber(), account.getAccountNumber());
    }

    @Test
    void updateAccountGivenNoUpdateInDtoExpectThrowNoUpdateInUpdatedAccountException() {
        // setup
        AccountDto accountDto = AccountDto.builder()
                .id(1)
                .accountNumber("323245464")
                .balance(42500.5)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.SAVINGS)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE)
                .build();

        Account currentAccount = Account.builder()
                .id(accountDto.getId())
                .accountNumber(accountDto.getAccountNumber())
                .balance(accountDto.getBalance())
                .currency(accountDto.getCurrency())
                .accountType(accountDto.getAccountType())
                .openingDate(accountDto.getOpeningDate())
                .activeStatus(accountDto.getActiveStatus().getValue())
                .build();

        Mockito.when(accountRepository.findById(accountDto.getId())).thenReturn(Optional.of(currentAccount));

        // when & then
        NoChangeInUpdatedAccountException ex = assertThrows(NoChangeInUpdatedAccountException.class, () -> accountService.updateAccount(accountDto));

        assertEquals(ExceptionMessageConstant.NO_CHANGE_IN_UPDATED_ACCOUNT.getMessage(), ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getHttpStatus());
    }

    @Test
    void updateAccountGivenCorrectAccountDtoExpectReturnAccountDto() {
        // setup
        AccountDto accountDto = AccountDto.builder()
                .id(1)
                .accountNumber("323245464")
                .balance(42500.5)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.SAVINGS)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE)
                .build();

        Account currentAccount = Account.builder()
                .id(1)
                .accountNumber("777325362")
                .balance(35000.0)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.CHECKING)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();

        Account updatedAccount = Account.builder()
                .id(accountDto.getId())
                .accountNumber(accountDto.getAccountNumber())
                .balance(accountDto.getBalance())
                .currency(accountDto.getCurrency())
                .accountType(accountDto.getAccountType())
                .openingDate(accountDto.getOpeningDate())
                .activeStatus(accountDto.getActiveStatus().getValue())
                .build();

        Mockito.when(accountRepository.findById(accountDto.getId())).thenReturn(Optional.of(currentAccount));
        Mockito.when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);

        // when
        AccountDto updatedAccountDto = accountService.updateAccount(accountDto);

        // then
        assertNotNull(updatedAccountDto);
        assertEquals(updatedAccountDto.getAccountNumber(), accountDto.getAccountNumber());
        assertEquals(updatedAccountDto.getAccountNumber(), updatedAccount.getAccountNumber());
    }

    @Test
    void deleteAccountGivenCorrectIdExpectReturnAccountDto() {
        // setup
        int givenId = 1;
        Account account = Account.builder().id(givenId).build();
        Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        // when
        accountService.deleteAccount(account.getId());
        // then
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(account.getId());
    }

    @Test
    void deleteAccountGivenIncorrectIdExpectThrowAccountNotFoundException() {
        // setup
        int givenId = -1;
        Mockito.when(accountRepository.findById(givenId)).thenReturn(Optional.empty());

        // when & then
        AccountNotFoundException ex = assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(givenId));

        assertEquals(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage(), ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    void addIncomeGivenCorrectIdAndCorrectAmountExpectReturnAccountDto() {
        // setup
        int givenId = 1;
        double firstBalance = 300;
        double givenAmount = 150;
        Account currentAccount = Account.builder()
                .id(1)
                .accountNumber("12345678")
                .balance(firstBalance)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.CHECKING)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();
        Account updatedAccount = Account.builder()
                .id(currentAccount.getId())
                .accountNumber(currentAccount.getAccountNumber())
                .balance(currentAccount.getBalance() + givenAmount)
                .currency(currentAccount.getCurrency())
                .accountType(currentAccount.getAccountType())
                .openingDate(currentAccount.getOpeningDate())
                .activeStatus(currentAccount.getActiveStatus())
                .build();
        Mockito.when(accountRepository.findById(givenId)).thenReturn(Optional.of(currentAccount));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(updatedAccount);
        // when
        AccountDto updatedAccountDto = accountService.addIncome(givenId, givenAmount);
        // then
        assertNotNull(updatedAccountDto);
        assertEquals(updatedAccountDto.getAccountNumber(), updatedAccount.getAccountNumber());
        assertTrue(updatedAccountDto.getBalance() > firstBalance);
    }

    @Test
    void addIncomeGivenIncorrectIdAndCorrectAmountExpectThrowAccountNotFoundException() {
        // setup
        int givenId = 1;
        double givenAmount = 150;
        Mockito.when(accountRepository.findById(givenId)).thenReturn(Optional.empty());

        // when & then
        AccountException ex = assertThrows(AccountNotFoundException.class, () -> accountService.addIncome(givenId, givenAmount));

        assertEquals(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage(), ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    void addExpenditureGivenIncorrectIdAndCorrectAmountExpectThrowAccountNotFoundException() {
        // setup
        int accountId = -1;
        double expenditureAmount = 250;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // when & then
        AccountException ex = assertThrows(AccountNotFoundException.class, () -> accountService.addExpenditure(accountId, expenditureAmount));

        assertEquals(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage(), ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    void addExpenditureGivenCorrectIdAndIncorrectAmountExpectThrowInsufficientBalanceException() {
        // setup
        int accountId = 1;
        double firstBalance = 300;
        double expenditureAmount = 350;
        Account currentAccount = Account.builder()
                .id(accountId)
                .accountNumber("12345678")
                .balance(firstBalance)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.CHECKING)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(currentAccount));

        // when & then
        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class, () -> accountService.addExpenditure(accountId, expenditureAmount));

        assertEquals(ExceptionMessageConstant.INSUFFICIENT_BALANCE.getMessage(), ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void addExpenditureGivenCorrectIdAndCorrectAmountExpectThrowInsufficientBalanceException() {
        // setup
        int accountId = 1;
        double firstBalance = 300;
        double expenditureAmount = 250;
        Account currentAccount = Account.builder()
                .id(accountId)
                .accountNumber("12345678")
                .balance(firstBalance)
                .currency(CurrencyConstant.AZN)
                .accountType(AccountTypeConstant.CHECKING)
                .openingDate(new Date())
                .activeStatus(ActiveStatusConstant.ACTIVE.getValue())
                .build();
        Account updatedAccount = Account.builder()
                .id(currentAccount.getId())
                .accountNumber(currentAccount.getAccountNumber())
                .balance(currentAccount.getBalance() - expenditureAmount)
                .currency(currentAccount.getCurrency())
                .accountType(currentAccount.getAccountType())
                .openingDate(currentAccount.getOpeningDate())
                .activeStatus(currentAccount.getActiveStatus())
                .build();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(currentAccount));
        Mockito.when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);
        // when
        AccountDto accountDto = accountService.addExpenditure(accountId, expenditureAmount);
        // then
        assertNotNull(accountDto);
        assertEquals(accountDto.getAccountNumber(), currentAccount.getAccountNumber());
        assertTrue(accountDto.getBalance() < firstBalance);
    }
}