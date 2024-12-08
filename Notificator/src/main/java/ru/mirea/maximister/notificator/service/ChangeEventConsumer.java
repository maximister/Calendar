package ru.mirea.maximister.notificator.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.dto.kafka.ChangeEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeEventConsumer {
    private final ChangeEventProcessor changeEventProcessor;

    @KafkaListener(topics = "change-events", groupId = "notificator")
    public void consume(ChangeEvent changeEvent) {
        log.info("Received message: {}", changeEvent);
        changeEventProcessor.process(changeEvent);
    }
}
