package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Event {
    UUID id;
    long authorUid;
    String title;
    Optional<String> description;
    EventInterval interval;
    Optional<String> conferenceUrl;
    @Builder.Default
    List<EventParticipant> participants = List.of();
    EventPeriod period;
    OpennessPolicy opennessPolicy;
}