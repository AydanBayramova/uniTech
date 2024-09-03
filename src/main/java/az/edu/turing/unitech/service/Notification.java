package az.edu.turing.unitech.service;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import org.springframework.stereotype.Service;

@Service
public interface Notification {

    void sendAccountCreationNotification(AccountEntity accountEntity);

    void sendBalanceUpdateNotification(AccountEntity accountEntity);

    void sendPasswordChangeNotification(AccountEntity accountEntity);

    void sendPasswordResetNotification(AccountEntity accountEntity);

    void sendAccountDeleteNotification(AccountEntity accountEntity);

}
