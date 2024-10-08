package az.edu.turing.unitech.controller;

import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.dto.AccountToAccountRequest;
import az.edu.turing.unitech.model.enums.Status;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransferService transferService;

    @PutMapping("transferAccount")
    public ResponseEntity<String> accountToAccountTransfer(@Valid @RequestBody AccountToAccountRequest request) {
        try {
            transferService.accountToAccountTransfer(request);
            return ResponseEntity.status(HttpStatus.OK).body("Transfer successful");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

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
    public ResponseEntity<AccountDto> createAccount(
            @RequestBody AccountDto accountDto,
            @RequestHeader("Authorization") String token) {


        String jwtToken = token.substring(7);


        AccountDto createdAccount = accountService.createAccount(accountDto, jwtToken);


        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/status")
    public ResponseEntity<Page<AccountDto>> getAllAccountsPageable(
            @RequestParam Status status,
            @PageableDefault(size = 10) Pageable pageable)
    {
        Page<AccountDto> allByStatus = accountService.getAllByStatus(status, pageable);
        return new ResponseEntity<>(allByStatus, HttpStatus.OK);
    }

    @GetMapping("/deactivatedAccountByAccountNumber")
    public ResponseEntity<AccountDto> getAccountByNumber(@RequestParam String accountNumber) {
        Optional<AccountDto> deactivatedAccountByAccountNumber = accountService.getDeactivatedAccountByAccountNumber(accountNumber);
        return new ResponseEntity<>(deactivatedAccountByAccountNumber.get(), HttpStatus.OK);
    }

    @GetMapping("/getAllDeactiveAccount")
    public ResponseEntity<List<AccountDto>> getAllDeactiveAccounts() {
        List<AccountDto> allDeactivateAccounts = accountService.getAllDeactivateAccounts();
        return new ResponseEntity<>(allDeactivateAccounts, HttpStatus.OK);
    }

}
