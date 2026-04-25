package ru.aston.notification.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.aston.notification.api.SendMailRequest;

/**
 * REST API для отправки уведомлений.
 *
 * <p>Принимает DTO запросов на отправку уведомлений.</p>
 */
@RequestMapping("/api/notifications")
public interface NotificationController {

    /**
     * Отправить email-уведомление пользователю.
     *
     * @param request данные уведомления (операция и email)
     * @return ответ 202 если запрос на отправку принят в обработку
     */
    @PostMapping("/email")
    ResponseEntity<Void> sendEmail(@Valid @RequestBody SendMailRequest request);
}