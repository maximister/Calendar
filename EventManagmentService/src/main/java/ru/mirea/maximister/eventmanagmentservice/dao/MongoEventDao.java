package ru.mirea.maximister.eventmanagmentservice.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import ru.mirea.maximister.eventmanagmentservice.dao.model.MongoEvent;
import ru.mirea.maximister.eventmanagmentservice.domain.Event;
import ru.mirea.maximister.eventmanagmentservice.domain.EventMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

@RequiredArgsConstructor
public class MongoEventDao implements EventDao {

    private final MongoCollection<MongoEvent> collection;

    @Override
    public List<Event> findByAuthorUid(long uid) {
        return collection.find(eq("authorUid", uid))
                .into(new ArrayList<>())
                .stream()
                .map(EventMapper::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByParticipantUid(long uid) {
        return collection.find(eq("participants.uid", uid))
                .into(new ArrayList<>())
                .stream()
                .map(EventMapper::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByUid(long uid) {
        return collection.find(or(
                        eq("participants.uid", uid),
                        eq("authorUid", uid)
                ))
                .into(new ArrayList<>())
                .stream()
                .map(EventMapper::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> findByUid(long uid, Instant from, Instant to) {
        return collection.find(
                        and(
                                or(
                                        eq("participants.uid", uid),
                                        eq("authorUid", uid)
                                ),
                                gte("interval.start", from),
                                lte("interval.end", to)
                        )
                )
                .into(new ArrayList<>())
                .stream()
                .map(EventMapper::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> findById(UUID uuid) {
        var event = collection.find(eq("_id", uuid))
                .map(EventMapper::from)
                .first();
        return Optional.ofNullable(event);
    }

    @Override
    public boolean upsert(Event event) {
        UpdateResult result = collection.replaceOne(
                eq("_id", event.getId()),
                EventMapper.to(event),
                new ReplaceOptions().upsert(true)
        );

        return result.getMatchedCount() > 0 || result.getUpsertedId() != null;
    }

    @Override
    public boolean delete(Event event) {
        return collection.deleteOne(
                eq("_id", event.getId())
        ).getDeletedCount() > 0;
    }
}