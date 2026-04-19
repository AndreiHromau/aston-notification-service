package ru.aston.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.notification.api.SendMailRequest;
import ru.aston.notification.service.NotificationService;

@RestController
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService notificationService;

    public NotificationControllerImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<Void> sendEmail(SendMailRequest request) {
        notificationService.sendUserNotification(request.operation(), request.email());
        return ResponseEntity.accepted().build();
    }
}