package ru.mirea.maximister.notificator.scedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.mirea.maximister.notificator.notifications.MailNotificator;
import ru.mirea.maximister.notificator.repository.ReminderRepository;
import ru.mirea.maximister.notificator.service.ReminderService;
import ru.mirea.maximister.notificator.utils.NotificationMapper;

import java.time.Duration;
import java.time.Instant;


@Component
@Slf4j
@RequiredArgsConstructor
public class ReminderNotifyTask {
    private final ReminderService reminderService;
    private final ReminderRepository reminderRepository;
    private final MailNotificator notificator;
    private final NotificationMapper notificationMapper;

    private static final Duration NOTIFICATION_WINDOW = Duration.ofMinutes(15);


    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleNotificatorTask() {
        var now = Instant.now();
        var needToNotify =
                reminderService.getNeedToNotifyReminders(now, now.plus(NOTIFICATION_WINDOW));

        if (needToNotify.isEmpty()) {
            log.info("Nothing to notify. Finish notification task");
            return;
        }

        for (var reminder: needToNotify) {
            var notificationData = notificationMapper.toNotificationData(reminder);
            var notification = notificationMapper.toNotification(notificationData);
            notificator.notify(notification);

            reminder.setProcessed(true);
            reminderRepository.save(reminder);
            log.info("Processed notification for email={} ievent={}",
                    reminder.getEmail(), reminder.getEventId());
        }

        log.debug("Successfully finished notification task");
    }
}
