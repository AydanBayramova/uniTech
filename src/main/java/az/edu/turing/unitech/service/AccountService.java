package az.edu.turing.unitech.service;

import az.edu.turing.unitech.model.dto.AccountDto;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto updateAccount(Long id, AccountDto accountDto);

    AccountDto transferToOwnAccount(String accountNumber, Double amount);

    AccountDto transferToAnotherAccount(String accountNumber, String pin, Double amount);

    void deleteAccountById(Long id);

    List<AccountDto> getAllAccounts();

    List<AccountDto> getAllDeactivateAccounts();

    Optional<AccountDto> getActiveAccountByAccountNumber(String accountNumber);

    Optional<AccountDto> getDeactivatedAccountByAccountNumber(String accountNumber);

    AccountDto addBalance(String accountNumber, Double amount);
}
