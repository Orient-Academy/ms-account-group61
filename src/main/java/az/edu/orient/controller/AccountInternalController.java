package az.edu.orient.controller;

import az.edu.orient.dto.AccountDto;
import az.edu.orient.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("internal/accounts")
public class AccountInternalController {

    private final AccountService accountService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        accountDto.setId(null);
        AccountDto createdAccount = accountService.createAccountInternal(accountDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

}
