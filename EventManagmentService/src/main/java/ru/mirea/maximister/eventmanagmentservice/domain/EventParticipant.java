package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventParticipant {
    long uid;
    String name;
    String surname;
    String email;
    ParticipationFormat participationFormat;
    boolean isFree;
}
