package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.exception.EmailSendingException;
import az.edu.turing.unitech.service.Notification;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@RequiredArgsConstructor
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
                + "Thank you, Turing Bank.";

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
    public void sendPasswordChangeNotification(AccountEntity accountEntity) {

        String email = accountEntity.getUser().getEmail();
        String subject = "Password Change";
        String message = "Dear " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Your account has been successfully changed.\n"
                + "Your account number: " + accountEntity.getAccountNumber() + "\n"
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

    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            mailSender.send(mimeMessage);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new EmailSendingException("Email can't be sending" + e);
        }
    }
}
