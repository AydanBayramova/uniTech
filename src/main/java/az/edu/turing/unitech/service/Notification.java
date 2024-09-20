package az.edu.turing.unitech.service;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface Notification {

    void sendAccountCreationNotification(AccountEntity accountEntity);

    void sendBalanceUpdateNotification(AccountEntity accountEntity);

    void sendPasswordChangeNotification(UserEntity accountEntity);

    void sendUpdateAccount(AccountEntity accountEntity);

    void sendAccountDeleteNotification(AccountEntity accountEntity);

    void sendAccountByNumberNotification(AccountEntity accountEntity,String accountNumber);

}
