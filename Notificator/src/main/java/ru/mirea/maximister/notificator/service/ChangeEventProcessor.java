package ru.mirea.maximister.notificator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.client.EventClient;
import ru.mirea.maximister.client.UserClient;
import ru.mirea.maximister.notificator.model.db.Reminder;
import ru.mirea.maximister.dto.kafka.ChangeEvent;
import ru.mirea.maximister.dto.kafka.ChangeEventType;
import ru.mirea.maximister.notificator.notifications.MailNotificator;
import ru.mirea.maximister.notificator.repository.ReminderRepository;
import ru.mirea.maximister.notificator.utils.NotificationMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeEventProcessor {
    private final UserClient userClient;
    private final EventClient eventClient;
    private final MailNotificator notificator;
    private final ReminderRepository reminderRepository;
    private final NotificationMapper notificationMapper;

    public void process(ChangeEvent changeEvent) {
        var type = changeEvent.eventType();

        if (type == ChangeEventType.CREATE_EVENT) {
            addReminders(changeEvent);
        }

        if (type == ChangeEventType.DELETE_EVENT) {
            // remove all unactual reminders for already removed event
            reminderRepository.deleteAllByEventId(changeEvent.event());
        }

        var event = eventClient.getEventById(changeEvent.event());
        var users = changeEvent.uids().stream().map(userClient::getUserByUid).toList();

        for (var user: users) {
            var notification = notificationMapper.toNotification(
                    notificationMapper.toNotificationData(changeEvent, user, event)
            );

            notificator.notify(notification);
            log.info("notified user {}", user.getEmail());
        }
    }

    public void addReminders(ChangeEvent changeEvent) {
        var event = eventClient.getEventById(changeEvent.event());
        var users = changeEvent.uids().stream().map(userClient::getUserByUid).toList();

        for (var user: users) {
            var reminder = Reminder.builder()
                    .email(user.getEmail())
                    .eventId(event.getId().toString())
                    .processed(false)
                    .startDate(event.getFrom())
                    .build();
            reminderRepository.save(reminder);
            log.info("Added reminder for email {}", user.getEmail());
        }
    }
}
