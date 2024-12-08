package ru.mirea.maximister.notificator.model;

import lombok.Builder;

@Builder
public record Notification(
        String title,
        String message,
        String to
) {
}
