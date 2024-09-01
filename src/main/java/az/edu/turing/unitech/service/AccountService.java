package az.edu.turing.unitech.service;

import az.edu.turing.unitech.model.dto.AccountDto;
import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto updateAccount(Long id, AccountDto accountDto);


    AccountDto transferToOwnAccount(String accountNumber, Double amount);


    AccountDto transferToAnotherAccount(String cardNumber, String pin, Double amount);


    void deleteAccountById(Long id);


    List<AccountDto> getAllAccounts();


    AccountDto addBalance(String accountNumber, Double amount);
}
