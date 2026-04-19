package ru.aston.notification.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.aston.notification.kafka.dto.UserEvent;

@Service
public class UserEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);

    @KafkaListener(
            topics = "${app.kafka.user-events-topic}",
            containerFactory = "userEventKafkaListenerContainerFactory"
    )
    public void onMessage(UserEvent event) {
        log.info("Consumed user event: operation={} email={}", event.operation(), event.email());
    }
}