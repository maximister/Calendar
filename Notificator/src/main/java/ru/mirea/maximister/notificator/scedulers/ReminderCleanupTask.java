package ru.mirea.maximister.notificator.scedulers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.mirea.maximister.notificator.service.ReminderService;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderCleanupTask {
    private final ReminderService reminderService;

    private static final Duration TTL = Duration.ofDays(1);

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleReminderCleanup() {
        Instant now = Instant.now();
        Instant ttlDate = now.minus(TTL);

        boolean deleted = reminderService.cleanRemindersBeforeDate(ttlDate);
        if (deleted) {
            log.info("Old reminders cleaned up successfully.");
        } else {
            log.info("No old reminders to clean.");
        }
    }
}

