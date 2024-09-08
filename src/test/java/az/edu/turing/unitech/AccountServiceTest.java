package az.edu.turing.unitech;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.domain.repository.AccountRepository;
import az.edu.turing.unitech.exception.AccountNotFoundException;
import az.edu.turing.unitech.model.dto.AccountDto;
import az.edu.turing.unitech.model.enums.Status;
import az.edu.turing.unitech.model.mapper.AccountMapper;
import az.edu.turing.unitech.service.AccountService;
import az.edu.turing.unitech.service.Notification;
import jakarta.mail.Session;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class AccountServiceTest {
    @Mock
    private EntityManager em;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private Filter filter;

    @Mock
    private Session session;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AccountService accountService;

    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts(){
        List<AccountEntity> mockAccountEntities = new ArrayList<>();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        mockAccountEntities.add(accountEntity);

        List<AccountDto> mockAccountDtos = new ArrayList<>();
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(1L);
        mockAccountDtos.add(accountDto);

        when(accountRepository.findAll()).thenReturn(mockAccountEntities);
        when(accountMapper.accountEntityListToAccountDtoList(mockAccountEntities)).thenReturn(mockAccountDtos);

        List<AccountDto> result=accountService.getAllAccounts();

        assertEquals(mockAccountDtos,result);
        assertEquals(1,result.size());
    }

    @Test
    void testDeleteAccountByAccountNumber_AccountNotFound() {

        when(accountRepository.findByAccountNumberAndStatus("12345", Status.ACTIVE)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccountByAccountNumber("12345"));

        verify(accountRepository).findByAccountNumberAndStatus("12345", Status.ACTIVE);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void testDeleteAccountByAccountNumber_Succes(){
        when(accountRepository.findByAccountNumberAndStatus("12345", Status.ACTIVE)).thenReturn(Optional.of(new AccountEntity()));
        accountService.deleteAccountByAccountNumber("12345");

        verify(accountRepository).findByAccountNumberAndStatus("12345", Status.ACTIVE);
        verify(accountRepository).deleteByAccountNumber("12345");
    }

    @Test
    void testAddBalance_Success() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber("12345");
        accountEntity.setBalance(BigDecimal.valueOf(100.00));
        accountEntity.setStatus(Status.ACTIVE);

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        accountEntity.setUser(user);

        when(accountRepository.findByAccountNumberAndStatus("12345", Status.ACTIVE))
                .thenReturn(Optional.of(accountEntity));
        when(accountMapper.accountEntityToDto(any(AccountEntity.class)))
                .thenReturn(new AccountDto());

        AccountDto result = accountService.addBalance("12345", 50.00);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150.00), accountEntity.getBalance());
        verify(accountRepository).save(accountEntity);
        verify(notificationService).sendBalanceUpdateNotification(accountEntity);
    }

}
