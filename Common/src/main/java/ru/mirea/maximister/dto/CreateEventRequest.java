package ru.mirea.maximister.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateEventRequest {
    long uid;
    String title;
    @Nullable
    String description;
    Instant from;
    Instant to;
    @Nullable
    String conferenceUrl;
    String period;
    String opennessPolicy;
}
