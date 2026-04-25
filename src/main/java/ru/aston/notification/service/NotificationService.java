package ru.aston.notification.service;

import org.springframework.stereotype.Service;
import ru.aston.notification.mail.MailSender;

@Service
public class NotificationService {

    private final MailSender mailSender;

    public NotificationService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendUserNotification(String operation, String email) {
        String subject = "Notification";
        String body = switch (operation) {
            case "DELETE" -> "Здравствуйте! Ваш аккаунт был удалён.";
            case "CREATE" -> "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };

        mailSender.send(email, subject, body);
    }
}