package az.edu.turing.unitech.service;

import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.exception.IllegalArgumentException;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto updateAccount(Long id, AccountDto accountDto);

    void deleteAccountByAccountNumber(String accountNumber);

    List<AccountDto> getAllAccounts();

    List<AccountDto> getAllActiveAccounts();

    List<AccountDto> getAllDeactivateAccounts();

    Optional<AccountDto> getActiveAccountByAccountNumber(String accountNumber);

    Optional<AccountDto> getDeactivatedAccountByAccountNumber(String accountNumber);

    AccountDto addBalance(String accountNumber, Double amount);

    void changePassword(Long userId, String oldPassword, String newPassword);


    Page<AccountDto> getAllByStatus(Status status, Pageable pageable);


}
