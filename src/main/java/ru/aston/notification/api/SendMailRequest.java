package ru.aston.notification.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendMailRequest(
        @NotBlank String operation,
        @NotBlank @Email String email
) {
}