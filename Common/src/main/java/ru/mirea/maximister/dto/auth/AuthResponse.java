package ru.mirea.maximister.dto.auth;

import jakarta.validation.constraints.NotNull;

public record AuthResponse(
        String token
) {
}