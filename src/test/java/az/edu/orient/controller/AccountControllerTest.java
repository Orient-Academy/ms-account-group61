package az.edu.orient.controller;

import az.edu.orient.constant.AccountTypeConstant;
import az.edu.orient.constant.ActiveStatusConstant;
import az.edu.orient.constant.ExceptionMessageConstant;
import az.edu.orient.dto.AccountDto;
import az.edu.orient.exception.AccountException;
import az.edu.orient.exception.AccountNotFoundException;
import az.edu.orient.exception.InsufficientBalanceException;
import az.edu.orient.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllAccountsGivenAccountDtoListExpectReturnAccountDtoList() throws Exception {
        // setup
        List<AccountDto> accounts = new ArrayList<>();
        AccountDto accountDto = Instancio.create(AccountDto.class);
        checkAndCorrectBalance(accountDto);
        accountDto.setActiveStatus(ActiveStatusConstant.ACTIVE);
        accounts.add(accountDto);
        Mockito.when(accountService.getAllAccounts()).thenReturn(accounts);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts")).andReturn();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void getAccountByIdGivenExistedIdExpectReturnAccountDto() throws Exception {
        // setup
        int accountId = 1;
        AccountDto accountDto = Instancio.create(AccountDto.class);
        checkAndCorrectBalance(accountDto);
        accountDto.setActiveStatus(ActiveStatusConstant.ACTIVE);
        Mockito.when(accountService.getAccountById(accountId)).thenReturn(accountDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAccountByIdGivenNotExistingIdExpectThrowAccountNotFoundException() throws Exception {
        // setup
        int accountId = -1;
        RuntimeException ex = new AccountNotFoundException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage());
        Mockito.when(accountService.getAccountById(accountId)).thenThrow(ex);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accountId)).andReturn();

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void getAccountByAccountTypeIdGivenAccountTypeExpectReturnAccountDto() throws Exception {
        // setup
        AccountTypeConstant accountType = AccountTypeConstant.SAVINGS;
        AccountDto accountDto = Instancio.create(AccountDto.class);
        checkAndCorrectBalance(accountDto);
        accountDto.setActiveStatus(ActiveStatusConstant.ACTIVE);
        Mockito.when(accountService.getAccountsByAccountType(accountType)).thenReturn(List.of(accountDto));

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/type/" + accountType)
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        // then
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
    }

    @Test
    void createAccountGivenValidAccountDtoExpectReturnCreatedAccountDto() throws Exception {
        // setup
        AccountDto accountDto = Instancio.create(AccountDto.class);
        checkAndCorrectBalance(accountDto);
        accountDto.setActiveStatus(ActiveStatusConstant.ACTIVE);
        AccountDto createdAccountDto = accountDto.clone();
        Mockito.when(accountService.createAccount(Mockito.any(AccountDto.class))).thenReturn(createdAccountDto);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void updateAccountGivenExistingIdAndReturnAccountDto() throws Exception {
        // setup
        AccountDto accountDto = Instancio.create(AccountDto.class);
        checkAndCorrectBalance(accountDto);
        accountDto.setActiveStatus(ActiveStatusConstant.ACTIVE);
        AccountDto updatedAccountDto = accountDto.clone();
        Mockito.when(accountService.updateAccount(Mockito.any(AccountDto.class))).thenReturn(updatedAccountDto);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/accounts/" + accountDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void updateAccountGivenNotExistingIdAndThrowAccountNotFoundException() throws Exception {
        // setup
        int accountId = -1;
        AccountDto accountDto = Instancio.create(AccountDto.class);
        checkAndCorrectBalance(accountDto);
        accountDto.setActiveStatus(ActiveStatusConstant.ACTIVE);
        AccountNotFoundException exception = new AccountNotFoundException("Account not found!");
        Mockito.when(accountService.updateAccount(Mockito.any(AccountDto.class))).thenThrow(exception);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void addIncomeGivenCorrectAccountIdAndCorrectAmountExpectReturnIncomeDto() throws Exception {
        // setup
        int accountId = 1;
        double firstAmount = 42500.5;
        double updatedAmount = 43500;
        double income = updatedAmount - firstAmount;
        AccountDto serviceResponse = Instancio.create(AccountDto.class);
        serviceResponse.setBalance(updatedAmount);
        serviceResponse.setActiveStatus(ActiveStatusConstant.ACTIVE);

        Mockito.when(accountService.addIncome(accountId, income)).thenReturn(serviceResponse);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", String.valueOf(income)))
                .andReturn();

        // then
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
    }

    @Test
    void addIncomeGivenIncorrectAccountIdAndCorrectAmountExpectReturnIncomeDto() throws Exception {
        // setup
        int accountId = -1;
        double firstAmount = 42500.5;
        double updatedAmount = 43500;
        double income = updatedAmount - firstAmount;
        RuntimeException ex = new AccountNotFoundException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage());
        Mockito.when(accountService.addIncome(accountId, income))
                .thenThrow(ex);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount", String.valueOf(income)))
                .andReturn();

        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
    }

    @Test
    void addExpenditureGivenCorrectAndSufficientAmountAndExistingIdExpectReturnAccountDto() throws Exception {
        int accountId = 1;
        double expenditure = 2500;
        double firstAmount = 42500;
        double updatedAmount = firstAmount - expenditure;
        AccountDto serviceResponse = Instancio.create(AccountDto.class);
        serviceResponse.setBalance(updatedAmount);
        serviceResponse.setActiveStatus(ActiveStatusConstant.ACTIVE);

        Mockito.when(accountService.addExpenditure(accountId, expenditure)).thenReturn(serviceResponse);
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/expenditure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount",  String.valueOf(expenditure)))
                .andReturn();
        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void addExpenditureGivenInsufficientAmountAndExistingIdExpectThrowInsufficientBalanceException()
            throws Exception {
        int accountId = 1;
        double expenditure = 2500;
        AccountException ex = new InsufficientBalanceException(ExceptionMessageConstant.INSUFFICIENT_BALANCE.getMessage());
        Mockito.when(accountService.addExpenditure(accountId, expenditure)).thenThrow(ex);
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/expenditure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount",  String.valueOf(expenditure)))
                .andReturn();
        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }


    @Test
    void addExpenditureGivenIncorrectIdAndCorrectExpenditureExpectThrowAccountNotFoundException() throws Exception {
        int accountId = -1;
        Double expenditure = 123.0;
        AccountException ex = new AccountNotFoundException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage());
        Mockito.when(accountService.addExpenditure(accountId, expenditure)).thenThrow(ex);
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/" + accountId + "/expenditure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("amount",  String.valueOf(expenditure)))
                .andReturn();
        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void deleteAccountGivenCorrectIdExpectReturnString() throws Exception {
        // setup
        int accountId = 1;
        Mockito.doNothing().when(accountService).deleteAccount(accountId);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted successfully!"))
                .andReturn();
        Mockito.verify(accountService).deleteAccount(accountId);
    }

    @Test
    void deleteAccountGivenIncorrectIdExpectThrowAccountNotFoundException() throws Exception {
        // setup
        int accountId = -1;
        RuntimeException ex = new AccountNotFoundException(ExceptionMessageConstant.ACCOUNT_NOT_FOUND_MESSAGE.getMessage());
        Mockito.doThrow(ex).when(accountService).deleteAccount(accountId);
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/{id}", accountId))
                .andReturn();
        // then
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    void checkAndCorrectBalance(AccountDto accountDto){
        Double defaultCorrectBalance = 2000.0;
        if(accountDto.getBalance() < 0){
            accountDto.setBalance(defaultCorrectBalance);
        }
    }

}