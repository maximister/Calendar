package ru.mirea.maximister.eventmanagmentservice.service;

import ru.mirea.maximister.eventmanagmentservice.domain.Event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface EventService {
    String createEvent(Event event);

    Optional<Event> getEventById(String id);

    boolean deleteEvent(String eventId, long uid);

    boolean updateEvent(Event event, long uid);

    boolean addUserToEvent(String eventId, long uid, long initiatorUid);

    boolean deleteUserFromEvent(String eventId, long uid, long initiatorUid);

    List<Event> getAllUserEvents(long uid, Instant from, Instant to);
}
