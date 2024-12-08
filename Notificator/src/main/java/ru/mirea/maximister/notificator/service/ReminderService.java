package ru.mirea.maximister.notificator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.notificator.model.db.Reminder;
import ru.mirea.maximister.notificator.repository.ReminderRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public boolean cleanRemindersBeforeDate(Instant before) {
        return reminderRepository.deleteByStartDateBefore(before);
    }

    public List<Reminder> getNeedToNotifyReminders(Instant from, Instant to) {
        return reminderRepository.findByStartDateBetweenAndProcessed(from, to, false);
    }

    public boolean cleanUpRemovedEventReminders(String removedEventId) {
     return reminderRepository.deleteAllByEventId(removedEventId);
    }
}
