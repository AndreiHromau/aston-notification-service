package ru.aston.notification.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StubMailSender implements MailSender {

    private static final Logger log = LoggerFactory.getLogger(StubMailSender.class);

    private static final String LOG_SEND_MAIL_TEMPLATE =
            "Sending mail to={} subject={} body={}";

    @Override
    public void send(String to, String subject, String body) {
        log.info(LOG_SEND_MAIL_TEMPLATE, to, subject, body);
    }
}