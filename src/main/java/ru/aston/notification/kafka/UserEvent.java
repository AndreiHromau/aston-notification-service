package ru.aston.notification.kafka;

public record UserEvent(
        String operation,
        String email
) {
}