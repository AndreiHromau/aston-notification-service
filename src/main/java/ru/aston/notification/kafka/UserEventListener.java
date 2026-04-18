package ru.aston.notification.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.aston.notification.service.NotificationService;

@Service
public class UserEventListener {

    private final NotificationService notificationService;

    public UserEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "${app.kafka.user-events-topic}",
            containerFactory = "userEventKafkaListenerContainerFactory"
    )
    public void onMessage(UserEvent event) {
        notificationService.sendUserNotification(event.operation(), event.email());
    }
}