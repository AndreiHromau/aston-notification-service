package ru.aston.notification.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO-запрос на отправку уведомления на email.
 *
 * @param operation тип операции/события (например, CREATE, DELETE)
 * @param email     email получателя
 */
public record SendMailRequest(
        @NotBlank String operation,
        @NotBlank @Email String email
) {
}