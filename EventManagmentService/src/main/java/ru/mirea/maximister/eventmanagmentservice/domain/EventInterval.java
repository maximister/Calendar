package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.Builder;
import lombok.Value;
import ru.mirea.maximister.eventmanagmentservice.dao.model.MongoEventInterval;

import java.time.Duration;
import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class EventInterval {
    Instant start;
    Instant end;

    public Duration getDuration() {
        return Duration.between(start, end);
    }
}
