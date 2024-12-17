package ru.mirea.maximister.eventmanagmentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.maximister.dto.*;
import ru.mirea.maximister.eventmanagmentservice.domain.*;
import ru.mirea.maximister.eventmanagmentservice.service.EventService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/v2/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable String eventId) {
        var event = eventService.getEventById(eventId).orElseThrow();
        var participants = Objects.requireNonNullElse(
                event.getParticipants(),
                new ArrayList<EventParticipant>()
        );

        var dto = EventDto.builder()
                .id(event.getId())
                .authorUid(event.getAuthorUid())
                .title(event.getTitle())
                .description(event.getDescription().orElse(null))
                .to(event.getInterval().getEnd())
                .from(event.getInterval().getStart())
                .conferenceUrl(event.getConferenceUrl().orElse(null))
                .participants(
                        participants.stream()
                                .map(p -> ParticipantDto.builder()
                                        .uid(p.getUid())
                                        .name(p.getName())
                                        .surname(p.getSurname())
                                        .email(p.getEmail())
                                        .participationFormat(p.getParticipationFormat().name())
                                        .isFree(p.isFree())
                                        .build()
                                ).toList()
                )
                .period(event.getPeriod().name())
                .opennessPolicy(event.getOpennessPolicy().name())
                .build();

        return new ResponseEntity<>(dto, OK);
    }

    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody CreateEventRequest request) {
        var result = eventService.createEvent(
                Event.builder()
                        .title(request.getTitle())
                        .authorUid(request.getUid())
                        .description(Optional.ofNullable(request.getDescription()))
                        .interval(EventInterval.builder()
                                .start(request.getFrom())
                                .end(request.getTo())
                                .build()
                        )
                        .conferenceUrl(Optional.ofNullable(request.getConferenceUrl()))
                        .period(EventPeriod.valueOf(request.getPeriod()))
                        .opennessPolicy(OpennessPolicy.valueOf(request.getOpennessPolicy()))
                        .build()
        );

        return new ResponseEntity<>(result, OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteEvent(@RequestBody DeleteEventRequest request) {
        return new ResponseEntity<>(
                eventService.deleteEvent(
                request.getEventId(), request.getUid()),
                OK);
    }

    @PutMapping
    public ResponseEntity<Boolean> updateEvent(@RequestBody UpdateEventRequest request) {
        return new ResponseEntity<>(eventService.updateEvent(
                Event.builder()
                        .title(request.getEvent().getTitle())
                        .authorUid(request.getEvent().getAuthorUid())
                        .id(request.getEvent().getId())
                        .description(Optional.ofNullable(request.getEvent().getDescription()))
                        .interval(EventInterval.builder()
                                .start(request.getEvent().getFrom())
                                .end(request.getEvent().getTo())
                                .build()
                        )
                        .conferenceUrl(Optional.ofNullable(request.getEvent().getConferenceUrl()))
                        .period(EventPeriod.valueOf(request.getEvent().getPeriod()))
                        .opennessPolicy(OpennessPolicy.valueOf(request.getEvent().getOpennessPolicy()))
                        .participants(Objects.requireNonNullElse(
                                request.getEvent().getParticipants(),
                                        new ArrayList<ParticipantDto>()
                                )
                                .stream()
                                .map(p -> new EventParticipant(
                                        p.getUid(),
                                        p.getName(),
                                        p.getSurname(),
                                        p.getEmail(),
                                        ParticipationFormat.valueOf(p.getParticipationFormat()),
                                        p.isFree()
                                )).toList())
                        .build(),
                request.getUid()
        ),
                OK);
    }

    // add to clients
    @PutMapping("/users")
    public ResponseEntity<?> addUserToEvent(@RequestBody AddUserToEventRequest request) {
        return new ResponseEntity<>(eventService.addUserToEvent(
                request.getEventId(), request.getUid(), request.getInitiatorUid()),
                OK);
    }

    // add to clients
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUserFromEvent(@RequestBody DeleteUserFromEventRequest request) {
        return new ResponseEntity<>(eventService.deleteUserFromEvent(
                request.getEventId(), request.getUid(), request.getInitiatorUid()),
                OK);
    }

    @GetMapping("/users/{userUid}&{from}&{to}")
    public ResponseEntity<EventViewList> getAllUserEvents(
            @PathVariable long userUid,
            @PathVariable Instant from,
            @PathVariable Instant to
            ) {
        var result = eventService.getAllUserEvents(userUid, from, to);

        var view = EventViewList.builder()
                .eventViews(
                        result.stream()
                                .map(event -> EventView.builder()
                                        .id(event.getId().toString())
                                        .title(event.getTitle())
                                        .from(event.getInterval().getStart())
                                        .to(event.getInterval().getEnd())
                                        .build()
                                ).toList())
                .build();

        return new ResponseEntity<>(view, OK);
    }
}