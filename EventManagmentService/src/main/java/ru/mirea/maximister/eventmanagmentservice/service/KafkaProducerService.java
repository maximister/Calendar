package ru.mirea.maximister.eventmanagmentservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.dto.kafka.ChangeEvent;
import ru.mirea.maximister.dto.kafka.ChangeEventType;
import ru.mirea.maximister.eventmanagmentservice.domain.Event;
import ru.mirea.maximister.eventmanagmentservice.domain.EventParticipant;

import java.util.ArrayList;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "change-events";

    private final KafkaTemplate<String, ChangeEvent> kafkaTemplate;

    public void sendMessage(ChangeEvent changeEvent) {
        kafkaTemplate.send(TOPIC, changeEvent);
        log.info("Message sent: {}", changeEvent);
    }


    public static ChangeEvent mapChangeEvent(ChangeEventType changeEventType, Event event) {
        var uids = new ArrayList<>(
                event.getParticipants().stream()
                .map(EventParticipant::getUid)
                .toList()
        );

        uids.add(event.getAuthorUid());


        return ChangeEvent.builder()
                .eventType(changeEventType)
                .event(event.getId().toString())
                .uids(uids)
                .build();
    }
}

