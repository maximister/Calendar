package ru.mirea.maximister.notificator.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.client.EventClient;
import ru.mirea.maximister.client.UserClient;
import ru.mirea.maximister.dto.EventDto;
import ru.mirea.maximister.dto.UserDto;
import ru.mirea.maximister.notificator.model.Notification;
import ru.mirea.maximister.notificator.model.NotificationData;
import ru.mirea.maximister.notificator.model.NotificationType;
import ru.mirea.maximister.notificator.model.db.Reminder;
import ru.mirea.maximister.dto.kafka.ChangeEvent;
import ru.mirea.maximister.dto.kafka.ChangeEventType;
import ru.mirea.maximister.notificator.service.MailTemplateService;

@Service
@RequiredArgsConstructor
public class NotificationMapper {
    private final EventClient eventClient;
    private final UserClient userClient;
    private final MailTemplateService templateService;


    public Notification toNotification(NotificationData notificationData) {
        return Notification.builder()
                .title("Уведомление о предстоящем событии")
                .message(templateService.getTemplate(notificationData))
                .to(notificationData.user().getEmail())
                .build();
    }

    public NotificationData toNotificationData(Reminder reminder) {
        return NotificationData.builder()
                .notificationType(NotificationType.REMINDER)
                .user(userClient.getUserByEmail(reminder.getEmail()))
                .event(eventClient.getEventById(reminder.getEventId()))
                .build();
    }

    public NotificationData toNotificationData(ChangeEvent changeEvent, UserDto user, EventDto event) {
        return NotificationData.builder()
                .notificationType(toNotificationType(changeEvent.eventType()))
                .user(user)
                .event(event)
                .build();
    }

    public NotificationType toNotificationType(ChangeEventType changeEventType) {
        return switch (changeEventType) {
            case CREATE_EVENT -> NotificationType.CREATE;
            case MOVE_EVENT, CHANGE_EVENT -> NotificationType.UPDATE;
            case DELETE_EVENT -> NotificationType.DELETE;
            default -> throw new IllegalArgumentException();
        };
    }
}
