package ru.aston.notification.kafka.dto;

/**
 * DTO-событие для Kafka при операциях с пользователем.
 *
 * @param operation тип операции: CREATE, DELETE
 * @param email     email пользователя
 */
public record UserEvent(
        String operation,
        String email
) {
}