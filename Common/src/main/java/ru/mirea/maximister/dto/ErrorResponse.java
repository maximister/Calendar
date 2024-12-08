package ru.mirea.maximister.dto;

public record ErrorResponse(
        String message,
        int status,
        String details
) {
}