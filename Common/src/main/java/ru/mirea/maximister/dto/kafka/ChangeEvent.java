package ru.mirea.maximister.dto.kafka;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ChangeEvent(
        ChangeEventType eventType,
        List<Long> uids,
        String event
) {
}
