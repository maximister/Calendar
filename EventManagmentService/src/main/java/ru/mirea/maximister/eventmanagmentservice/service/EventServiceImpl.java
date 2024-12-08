package ru.mirea.maximister.eventmanagmentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mirea.maximister.client.UserClient;
import ru.mirea.maximister.dto.kafka.ChangeEventType;
import ru.mirea.maximister.eventmanagmentservice.dao.EventDao;
import ru.mirea.maximister.eventmanagmentservice.domain.Event;
import ru.mirea.maximister.eventmanagmentservice.domain.EventParticipant;
import ru.mirea.maximister.eventmanagmentservice.domain.ParticipationFormat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static ru.mirea.maximister.eventmanagmentservice.service.KafkaProducerService.mapChangeEvent;

@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private final UserClient userClient;
    private final KafkaProducerService kafkaProducerService;


    @Override
    public String createEvent(Event event) {
        validateUser(event.getAuthorUid());

        var createdEvent = event.toBuilder().id(UUID.randomUUID()).build();
        var result = eventDao.upsert(createdEvent);
        if (!result) {
            throw new IllegalArgumentException(
                    "Error during creating event for user " + event.getAuthorUid()
            );
        }

        kafkaProducerService.sendMessage(mapChangeEvent(ChangeEventType.CREATE_EVENT, createdEvent));
        return createdEvent.getId().toString();
    }

    @Override
    public Optional<Event> getEventById(String id) {
        return eventDao.findById(UUID.fromString(id));
    }

    @Override
    public boolean deleteEvent(String eventId, long uid) {
        var event = eventDao.findById(UUID.fromString(eventId)).orElseThrow(
                () -> new IllegalArgumentException("Can not find event with id " + eventId)
        );
        validateUser(event.getAuthorUid());

        if (!UserUtils.isUserAllowedToChangeEvent(uid, event)) {
            throw new IllegalArgumentException(
                    "User " + uid + " has no permission to change event " + eventId
            );
        }

        kafkaProducerService.sendMessage(mapChangeEvent(ChangeEventType.DELETE_EVENT, event));
        return eventDao.delete(event);
    }

    @Override
    public boolean updateEvent(Event event, long uid) {
        if (!UserUtils.isUserAllowedToChangeEvent(uid, event)) {
            throw new IllegalArgumentException(
                    "User " + uid + " has no permission to change event " + event.getId()
            );
        }

        kafkaProducerService.sendMessage(mapChangeEvent(ChangeEventType.CHANGE_EVENT, event));
        return eventDao.upsert(event);
    }

    @Override
    public boolean addUserToEvent(String eventId, long uid, long initiatorUid) {
        var event = eventDao.findById(UUID.fromString(eventId)).orElseThrow(
                () -> new IllegalArgumentException("Can not find event with id " + eventId)
        );
        var user = userClient.getUserByUid(uid);

        if (!(UserUtils.isUserAllowedToChangeEvent(initiatorUid, event) || uid == initiatorUid)) {
            throw new IllegalArgumentException(
                    "User " + initiatorUid + " has no permission to change event " + event.getId()
            );
        }

        var updatedEvent = event.toBuilder()
                .participants(
                        Stream.concat(
                                event.getParticipants().stream(),
                                Stream.of(EventParticipant.builder()
                                        .uid(user.getUid())
                                        .name(user.getName())
                                        .isFree(false)
                                        .surname(user.getSurname())
                                        .participationFormat(ParticipationFormat.ONLINE)
                                        .build()
                                )
                        ).toList()
                )
                .build();

        kafkaProducerService.sendMessage(mapChangeEvent(ChangeEventType.CHANGE_EVENT, event));
        return eventDao.upsert(updatedEvent);
    }

    @Override
    public boolean deleteUserFromEvent(String eventId, long uid, long initiatorUid) {
        var event = eventDao.findById(UUID.fromString(eventId)).orElseThrow(
                () -> new IllegalArgumentException("Can not find event with id " + eventId)
        );

        if (!(UserUtils.isUserAllowedToChangeEvent(initiatorUid, event) || uid == initiatorUid)) {
            throw new IllegalArgumentException(
                    "User " + initiatorUid + " has no permission to change event " + event.getId()
            );
        }

        var updatedEvent = event.toBuilder()
                .participants(
                        event.getParticipants().stream()
                                .filter(p -> p.getUid() != uid)
                                .toList()
                )
                .build();

        kafkaProducerService.sendMessage(mapChangeEvent(ChangeEventType.DELETE_EVENT, event));
        return eventDao.upsert(updatedEvent);
    }


    @Override
    public List<Event> getAllUserEvents(long uid, Instant from, Instant to) {
        return eventDao.findByUid(uid, from, to);
    }

    private void validateUser(long uid) {
        boolean validUser = userClient.getUserByUid(uid) != null;

        if (!validUser) {
            throw new IllegalArgumentException("User " + uid + " is not valid");
        }
    }
}
