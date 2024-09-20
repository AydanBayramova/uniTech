package az.edu.turing.unitech.service;

import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    List<AccountDto> getAllAccounts();

    List<AccountDto> getAllActiveAccounts();

    List<AccountDto> getAllDeactivateAccounts();

    void deleteAccountByAccountNumber(String accountNumber);

    Optional<AccountDto> getActiveAccountByAccountNumber(String accountNumber);

    Optional<AccountDto> getDeactivatedAccountByAccountNumber(String accountNumber);

    AccountDto addBalance(String accountNumber, Double amount);

    Page<AccountDto> getAllByStatus(Status status, Pageable pageable);
}
