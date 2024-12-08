package ru.mirea.maximister.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        String username,
        String password
) {
}