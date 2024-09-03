package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.enums.AccountStatus;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.UserService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final Notification notificationService;


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
    public void deleteAccountById(Long id) {

    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return List.of();
    }

    @Override
    public AccountDto addBalance(String accountNumber, Double amount) {
        return null;
    }

    private String generateUniqueAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }
}
