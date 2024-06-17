package az.edu.orient.controller;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.dto.AccountDto;
import az.edu.orient.service.AccountService;
import az.edu.orient.validation.annotation.ValidMoneyAmount;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final List<Validator> validators;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        Optional.ofNullable(binder.getTarget())
                .ifPresent(target ->
                        validators.stream()
                                .filter(v -> v.supports(target.getClass()))
                                .forEach(binder::addValidators)
                );
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") Integer id) {
        AccountDto account = accountService.getAccountById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping(path = "type/{accountType}", produces = MediaType.APPLICATION_JSON_VALUE) // Changed the path here
    public ResponseEntity<List<AccountDto>> getAccountsByType(@PathVariable("accountType") AccountTypeConstant accountType) { // Changed the method name
        List<AccountDto> accounts = accountService.getAccountsByAccountType(accountType);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        accountDto.setId(null);
        AccountDto createdAccount = accountService.createAccount(accountDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> updateAccount(@PathVariable @NonNull Integer id, @RequestBody AccountDto accountDto) {
        accountDto.setId(id);
        AccountDto updatedAccount = accountService.updateAccount(accountDto);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PatchMapping(path = "{id}/income", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> addIncome(@PathVariable Integer id, @RequestParam @ValidMoneyAmount Double amount) {
        AccountDto updatedAccount = accountService.addIncome(id, amount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PatchMapping(path = "{id}/expenditure", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> addExpenditure(@PathVariable Integer id, @RequestParam @ValidMoneyAmount Double amount) {
        AccountDto updatedAccount = accountService.addExpenditure(id, amount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAccount2(@PathVariable Integer id) {
        System.out.println("Hello");
        return new ResponseEntity<>("Deleted successfully!", HttpStatus.OK);
    }
}
