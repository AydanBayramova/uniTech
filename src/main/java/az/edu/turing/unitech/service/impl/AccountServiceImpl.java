package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.exception.AccountNotFoundException;
import az.edu.turing.unitech.exception.InvalidAmountException;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.dto.UserDto;
import az.edu.turing.unitech.model.enums.Status;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.Notification;
import az.edu.turing.unitech.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountMapper accountMapper;
    private final Notification notificationService;

    private final EntityManager em;

    private final PasswordEncoder passwordEncoder;

 


    @Override
    public AccountDto createAccount(AccountDto accountDto) {

        userService.verifyUser(accountDto.getUser().getId());

        String accountNumber = generateUniqueAccountNumber();
        accountDto.setAccountNumber(accountNumber);

        AccountEntity accountEntity = accountMapper.accountDtoToAccountEntity(accountDto);
        accountEntity.setCreatedAt(LocalDateTime.now());
        accountEntity.setUpdatedAt(LocalDateTime.now());
        accountEntity.setStatus(Status.ACTIVE);

        AccountEntity save = accountRepository.save(accountEntity);
        notificationService.sendAccountCreationNotification(save);

        return accountMapper.accountEntityToDto(save);
    }



    @Override
    public void deleteAccountByAccountNumber(String accountNumber) {
        accountRepository.findByAccountNumberAndStatus(accountNumber, Status.ACTIVE)
                .orElseThrow(() -> new AccountNotFoundException("NO SUCH ACCOUNT"));

        accountRepository.deleteByAccountNumber(accountNumber);
    }


    @Override
    public List<AccountDto> getAllAccounts() {
        List<AccountEntity> accountEntities = accountRepository.findAll();
        return accountMapper.accountEntityListToAccountDtoList(accountEntities);
    }
    @Override
    public List<AccountDto> getAllActiveAccounts() {
        Session session = em.unwrap(Session.class);
        Filter filter= session.enableFilter("statusFilter");
        filter.setParameter("status", Status.ACTIVE.toString());
        List<AccountDto> activeAccounts =accountMapper.accountEntityListToAccountDtoList(accountRepository.findAll());
        session.disableFilter("statusFilter");
        return activeAccounts;
    }

    @Override
    public List<AccountDto> getAllDeactivateAccounts() {
        Session session = em.unwrap(Session.class);
        Filter filter= session.enableFilter("statusFilter");
        filter.setParameter("status", Status.DEACTIVATE.toString());
        List<AccountDto> deactivatedAccounts=accountMapper.accountEntityListToAccountDtoList(accountRepository.findAll());
        session.disableFilter("statusFilter");
        return deactivatedAccounts;
    }

    @Override
    public Optional<AccountDto> getActiveAccountByAccountNumber(String accountNumber) {
        return accountRepository.
                findByAccountNumberAndStatus(accountNumber, Status.ACTIVE).
                map(accountMapper::accountEntityToDto);
    }

    @Override
    public Optional<AccountDto> getDeactivatedAccountByAccountNumber(String accountNumber) {
        return accountRepository.
                findByAccountNumberAndStatus(accountNumber, Status.DEACTIVATE).
                map(accountMapper::accountEntityToDto);
    }

    @Override
    public AccountDto addBalance(String accountNumber, Double amount) {
        Optional<AccountEntity> accountEntity = accountRepository.findByAccountNumberAndStatus(accountNumber, Status.ACTIVE);
        if (accountEntity.isEmpty()) {
            throw new AccountNotFoundException("Account with number " + accountNumber + " not found.");
        }
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException("Amount to add must be greater than zero.");
        }

        AccountEntity entity = accountEntity.get();
        entity.setBalance(entity.getBalance().add(BigDecimal.valueOf(amount)));
        accountRepository.save(entity);
        notificationService.sendBalanceUpdateNotification(entity);
        return accountMapper.accountEntityToDto(entity);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        AccountEntity account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        if (passwordEncoder.matches(newPassword, account.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password");
        }

        validateNewPassword(newPassword);

        String encode = passwordEncoder.encode(newPassword);

        account.setPassword(encode);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);

        notificationService.sendPasswordChangeNotification(account);

    }

    @Override
    public Page<AccountDto> getAllByStatus(Status status, Pageable pageable) {

        Page<AccountEntity> allByStatus = accountRepository.getAllByStatus(status, pageable);

        return allByStatus.map(accountMapper::accountEntityToDto);
    }


    private String generateUniqueAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

    private void validateNewPassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }
    }


}
