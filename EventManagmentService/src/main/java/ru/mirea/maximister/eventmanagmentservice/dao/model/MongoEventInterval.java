package ru.mirea.maximister.eventmanagmentservice.dao.model;

import lombok.*;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MongoEventInterval {
    private Instant start;
    private Instant end;
}
