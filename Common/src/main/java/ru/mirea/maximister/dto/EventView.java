package ru.mirea.maximister.dto;


import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class EventView {
    String id;
    String title;
    Instant from;
    Instant to;
}
