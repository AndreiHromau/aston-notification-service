package ru.aston.notification.mail;

/**
 * Абстракция отправки email-сообщений.
 */
public interface MailSender {

    /**
     * Отправляет email-сообщение.
     *
     * @param to      адрес получателя
     * @param subject тема письма
     * @param body    текст письма
     */
    void send(String to, String subject, String body);
}