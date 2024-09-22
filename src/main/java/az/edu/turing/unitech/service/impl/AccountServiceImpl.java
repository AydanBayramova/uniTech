package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.config.JwtTokenProvider;
import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.domain.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    private final Notification notification;

    private final EntityManager em;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public AccountDto createAccount(AccountDto accountDto, String token) {

        String userId = jwtTokenProvider.getUserIdFromToken(token);


        UserEntity user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        String accountNumber = generateUniqueAccountNumber();
        accountDto.setAccountNumber(accountNumber);


        AccountEntity accountEntity = accountMapper.accountDtoToAccountEntity(accountDto);
        accountEntity.setCreatedAt(LocalDateTime.now());
        accountEntity.setUpdatedAt(LocalDateTime.now());
        accountEntity.setStatus(Status.ACTIVE);


        accountEntity.setUser(user);


        AccountEntity savedAccount = accountRepository.save(accountEntity);


        notification.sendAccountCreationNotification(savedAccount);


        return accountMapper.accountEntityToDto(savedAccount);
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
        notification.sendBalanceUpdateNotification(entity);
        return accountMapper.accountEntityToDto(entity);
    }

    @Override
    public Page<AccountDto> getAllByStatus(Status status, Pageable pageable) {

        Page<AccountEntity> allByStatus = accountRepository.getAllByStatus(status, pageable);

        return allByStatus.map(accountMapper::accountEntityToDto);
    }


    private String generateUniqueAccountNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }




}
