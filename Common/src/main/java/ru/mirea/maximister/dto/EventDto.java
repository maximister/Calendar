package ru.mirea.maximister.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventDto {
    UUID id;
    long authorUid;
    String title;
    String description;
    Instant from;
    Instant to;
    String conferenceUrl;
    @Builder.Default
    List<ParticipantDto> participants = List.of();
    String period;
    String opennessPolicy;
}
