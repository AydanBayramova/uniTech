package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.domain.entity.UserEntity;
import az.edu.turing.unitech.exception.EmailSendingException;
import az.edu.turing.unitech.service.Notification;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class NotificationImpl implements Notification {

    private final JavaMailSender mailSender;

    @Override
    public void sendAccountCreationNotification(AccountEntity accountEntity) {
        String email = accountEntity.getUser().getEmail();
        String subject = "Account Successfully Created";
        String message = "Dear " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Your account has been successfully created.\n"
                + "Your account number: " + accountEntity.getAccountNumber() + "\n"
                + "Initial balance: " + accountEntity.getBalance() + "\n\n"
                + "Thank you.";

        sendEmail(email, subject, message);
    }

    @Override
    public void sendBalanceUpdateNotification(AccountEntity accountEntity) {

        String email = accountEntity.getUser().getEmail();
        String subject = "Balance Successfully Updated";
        String message = "Dear " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Your balance has been successfully updated.\n"
                + "Your account number: " + accountEntity.getAccountNumber() + "\n"
                + "Initial balance: " + accountEntity.getBalance() + "\n\n"
                + "Thank you, Turing Bank.";
        sendEmail(email, subject, message);
    }

    @Override
    public void sendPasswordChangeNotification(UserEntity userEntity) {

        String email = userEntity.getEmail();
        String subject = "Password Change";
        String message = "Dear " + userEntity.getFirstName() + ",\n\n"
                + "Your account has been successfully changed.\n"
                + "Your account number: " + userEntity.getPin()+ "\n"
                + "Thank you, Turing Bank.";

        sendEmail(email, subject, message);

    }

    @Override
    public void sendUpdateAccount(AccountEntity accountEntity) {
        String email = accountEntity.getUser().getEmail();
        String subject = "Account email update";
        String message = "Dear " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Your email has been successfully updated.\n"
                + "Your account number: " + accountEntity.getAccountNumber() + "\n"
                + "Thank you, Turing Bank.";
        sendEmail(email, subject, message);

    }

    @Override
    public void sendAccountDeleteNotification(AccountEntity accountEntity) {
        String email = accountEntity.getUser().getEmail();
        String subject = "Your Account Has Been Deleted";
        String message = "Dear " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Your account has been successfully deleted.\n"
                + "Your account number: " + accountEntity.getAccountNumber() + "\n"
                + "Your account balance: " + accountEntity.getBalance() + "\n\n"
                + "Thank you for trusting Turing Bank.";

        sendEmail(email, subject, message);
    }

    @Override
    public void sendAccountByNumberNotification(AccountEntity accountEntity, String accountNumber) {
        String email = accountEntity.getUser().getEmail();
        String subject = "Your account deActive by account number: " + accountEntity.getAccountNumber();
        String message = "Dear " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Your account has been successfully activated.\n"
                + "Your account number: " + accountEntity.getAccountNumber() + "\n"
                + "Your account balance: " + accountEntity.getStatus() + "\n\n"
                + "Thank you for trusting Turing Bank.";
        sendEmail(email, subject, message);

    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("");
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EmailSendingException("Failed to send email to " + to + ": " + e.getMessage());
        }
    }


}
