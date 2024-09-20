package az.edu.turing.unitech.controller;

import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

     @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AccountDto>> getAllActiveAccounts() {
        List<AccountDto> activeAccounts = accountService.getAllActiveAccounts();
        return new ResponseEntity<>(activeAccounts, HttpStatus.OK);
    }


    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
           accountService.deleteAccountByAccountNumber(accountNumber);
           return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountDto> addBalance(@PathVariable String accountNumber, @RequestBody Double amount) {
        AccountDto updatedAccount=accountService.addBalance(accountNumber,amount);
        return new ResponseEntity<>(updatedAccount,HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto account) {
        AccountDto account1 = accountService.createAccount(account);
        return new ResponseEntity<>(account1,HttpStatus.CREATED);
    }



}
