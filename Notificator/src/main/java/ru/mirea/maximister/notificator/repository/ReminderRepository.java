package ru.mirea.maximister.notificator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.maximister.notificator.model.db.Reminder;

import java.time.Instant;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, String> {
     List<Reminder> findByStartDateBetweenAndProcessed(Instant from, Instant to, boolean processed);

     boolean deleteByStartDateBefore(Instant startDate);

     boolean deleteAllByEventId(String eventId);
}
