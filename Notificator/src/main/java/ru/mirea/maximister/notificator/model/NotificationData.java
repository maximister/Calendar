package ru.mirea.maximister.notificator.model;

import lombok.Builder;
import ru.mirea.maximister.dto.EventDto;
import ru.mirea.maximister.dto.UserDto;

@Builder(toBuilder = true)
public record NotificationData(
        NotificationType notificationType,
        UserDto user,
        EventDto event
) {
}
