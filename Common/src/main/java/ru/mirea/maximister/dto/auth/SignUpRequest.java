package ru.mirea.maximister.dto.auth;

public record SignUpRequest(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
) {}
