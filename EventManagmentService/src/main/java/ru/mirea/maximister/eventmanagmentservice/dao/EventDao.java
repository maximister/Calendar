package ru.mirea.maximister.eventmanagmentservice.dao;

import ru.mirea.maximister.eventmanagmentservice.domain.Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventDao {
     List<Event> findByAuthorUid(long uid);

     List<Event> findByParticipantUid(long uid);

     List<Event> findByUid(long uid);

     List<Event> findByUid(long uid, Instant from, Instant to);

     Optional<Event> findById(UUID uuid);

     boolean upsert(Event event);

     boolean delete(Event event);
}
