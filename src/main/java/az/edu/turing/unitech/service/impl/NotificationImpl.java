package az.edu.turing.unitech.service.impl;

import az.edu.turing.unitech.domain.entity.AccountEntity;
import az.edu.turing.unitech.service.Notification;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


import javax.mail.MessagingException;


@RequiredArgsConstructor
public class NotificationImpl implements Notification {

    private final JavaMailSender mailSender;
    @Override
    public void sendAccountCreationNotification(AccountEntity accountEntity) {
        String email = accountEntity.getUser().getEmail();
        String subject = "Hesab Uğurla Yaradıldı";
        String message = "Hörmətli " + accountEntity.getUser().getFirstName() + ",\n\n"
                + "Hesabınız uğurla yaradıldı.\n"
                + "Hesab nömrəniz: " + accountEntity.getAccountNumber() + "\n"
                + "Başlanğıc balans: " + accountEntity.getBalance() + "\n\n"
                + "Təşəkkürlər, Turing Bank.";



    }

    @Override
    public void sendBalanceUpdateNotification(AccountEntity accountEntity) {

    }

    @Override
    public void sendPasswordChangeNotification(AccountEntity accountEntity) {

    }

    @Override
    public void sendPasswordResetNotification(AccountEntity accountEntity) {

    }

    @Override
    public void sendAccountDeleteNotification(AccountEntity accountEntity) {

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
        }
    }
}
