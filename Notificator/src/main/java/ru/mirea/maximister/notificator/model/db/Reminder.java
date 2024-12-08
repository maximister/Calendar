package ru.mirea.maximister.notificator.model.db;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Reminder {
    @Id
    private String email;

    private String eventId;

    private Instant startDate;

    boolean processed;
}

