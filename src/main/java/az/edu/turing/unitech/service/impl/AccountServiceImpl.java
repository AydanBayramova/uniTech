package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.exception.AccountNotFoundException;
import az.edu.turing.unitech.exception.CannotDeleteActiveAccountException;
import az.edu.turing.unitech.exception.InvalidAmountException;
import az.edu.turing.unitech.exception.NoActiveAccountsFoundException;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.enums.AccountStatus;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import java.math.BigDecimal;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final Notification notificationService;
    private final AccountStatus accountStatus;
    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        userService.verifyUser(accountDto.getUser().getId());

        String accountNumber = generateUniqueAccountNumber();
        accountDto.setAccountNumber(accountNumber);

        AccountEntity accountEntity = accountMapper.accountDtoToAccountEntity(accountDto);
        accountEntity.setCreatedAt(LocalDateTime.now());
        accountEntity.setUpdatedAt(LocalDateTime.now());
        accountEntity.setStatus(AccountStatus.ACTIVE);

        AccountEntity save = accountRepository.save(accountEntity);
        notificationService.sendAccountCreationNotification(save);

        return accountMapper.accountEntityToDto(save);
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        return null;
    }

    @Override
    public AccountDto transferToOwnAccount(String accountNumber, Double amount) {
        return null;
    }

    @Override
    public AccountDto transferToAnotherAccount(String cardNumber, String pin, Double amount) {
        return null;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')") // For only admins
    public void deleteAccountById(Long id) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
        if (accountEntity.getStatus() == AccountStatus.ACTIVE) {
            throw new CannotDeleteActiveAccountException("Active accounts cannot be deleted.");
        }
        AccountDto accountDto = accountMapper.accountEntityToDto(accountEntity);
        accountRepository.delete(accountEntity);
        postDeleteOperations(accountDto);
    }

    //Operations will be performed after the account is deleted
    private void postDeleteOperations(AccountDto accountDto) {

    }

    @Override
    public List<AccountDto> getAllActiveAccounts() {
        List<AccountEntity> accountEntities = accountRepository.findAll();
        List<AccountEntity> activeAccounts=accountEntities.stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .collect(Collectors.toList());

        if(activeAccounts.isEmpty()){
            throw new NoActiveAccountsFoundException("No active accounts found.");
        }

        List<AccountDto> accountDtos=accountMapper.accountEntityListToAccountDtoList(activeAccounts);
        return accountDtos;
    }

    @Override
    public List<AccountDto> getAllAccounts() {
          List<AccountEntity> accountEntities = accountRepository.findAll();
          List<AccountDto> accountdtos= accountMapper.accountEntityListToAccountDtoList(accountEntities);
          return accountdtos;
    }


    @Override
    public AccountDto addBalance(String accountNumber, Double amount) {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);
        if (accountEntity == null) {
            throw new AccountNotFoundException("Account with number " + accountNumber + " not found");
        }
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException("Amount to add must be greater than zero.");
        }
        BigDecimal newBalance = accountEntity.getBalance().add(BigDecimal.valueOf(amount));
        accountRepository.save(accountEntity);
        return accountMapper.accountEntityToDto(accountEntity);
    }

    private String generateUniqueAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

}
