package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.experimental.UtilityClass;
import ru.mirea.maximister.eventmanagmentservice.dao.model.*;

import java.util.Optional;

@UtilityClass
public class EventMapper {
    public static MongoEvent to(Event event) {
        return MongoEvent.builder()
                .id(event.getId())
                .authorUid(event.getAuthorUid())
                .description(event.getDescription().orElse(null))
                .title(event.getTitle())
                .interval(to(event.getInterval()))
                .conferenceUrl(event.getConferenceUrl().orElse(null))
                .participants(
                        event.getParticipants().stream()
                                .map(EventMapper::to)
                                .toList()
                ).period(to(event.getPeriod()))
                .opennessPolicy(to(event.getOpennessPolicy()))
                .build();

    }

    public static Event from(MongoEvent event) {
        return Event.builder()
                .id(event.getId())
                .authorUid(event.getAuthorUid())
                .description(Optional.ofNullable(event.getDescription()))
                .title(event.getTitle())
                .interval(from(event.getInterval()))
                .conferenceUrl(Optional.ofNullable(event.getConferenceUrl()))
                .participants(
                        event.getParticipants().stream()
                                .map(EventMapper::from)
                                .toList()
                ).period(from(event.getPeriod()))
                .opennessPolicy(from(event.getOpennessPolicy()))
                .build();

    }

    public static MongoEventInterval to(EventInterval eventInterval) {
        return MongoEventInterval.builder()
                .start(eventInterval.getStart())
                .end(eventInterval.getEnd())
                .build();
    }

    public static EventInterval from(MongoEventInterval eventInterval) {
        return EventInterval.builder()
                .start(eventInterval.getStart())
                .end(eventInterval.getEnd())
                .build();
    }

    public static MongoEventParticipant to(EventParticipant eventParticipant) {
        return MongoEventParticipant.builder()
                .uid(eventParticipant.getUid())
                .name(eventParticipant.getName())
                .surname(eventParticipant.getSurname())
                .email(eventParticipant.getEmail())
                .participationFormat(to(eventParticipant.getParticipationFormat()))
                .isFree(eventParticipant.isFree())
                .build();
    }

    public static EventParticipant from(MongoEventParticipant eventParticipant) {
        return EventParticipant.builder()
                .uid(eventParticipant.getUid())
                .name(eventParticipant.getName())
                .surname(eventParticipant.getSurname())
                .email(eventParticipant.getEmail())
                .participationFormat(from(eventParticipant.getParticipationFormat()))
                .isFree(eventParticipant.isFree())
                .build();
    }

    public static MongoEventPeriod to(EventPeriod eventPeriod) {
        return MongoEventPeriod.valueOf(eventPeriod.name());
    }

    public static EventPeriod from(MongoEventPeriod mongoEventPeriod) {
        return EventPeriod.valueOf(mongoEventPeriod.name());
    }

    public static MongoOpennessPolicy to(OpennessPolicy opennessPolicy) {
        return MongoOpennessPolicy.valueOf(opennessPolicy.name());
    }

    public static OpennessPolicy from(MongoOpennessPolicy mongoOpennessPolicy) {
        return OpennessPolicy.valueOf(mongoOpennessPolicy.name());
    }

    public static MongoParticipationFormat to(ParticipationFormat participationFormat) {
        return MongoParticipationFormat.valueOf(participationFormat.name());
    }

    public static ParticipationFormat from(MongoParticipationFormat participationFormat) {
        return ParticipationFormat.valueOf(participationFormat.name());
    }
}
