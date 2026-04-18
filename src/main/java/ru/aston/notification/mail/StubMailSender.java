package ru.aston.notification.mail;

import org.springframework.stereotype.Service;

@Service
public class StubMailSender implements MailSender {

    @Override
    public void send(String to, String subject, String body) {
        System.out.println("Sending mail to=" + to + " subject=" + subject + " body=" + body);
    }
}