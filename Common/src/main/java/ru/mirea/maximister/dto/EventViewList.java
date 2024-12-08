package ru.mirea.maximister.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class EventViewList {
    List<EventView> eventViews;
}
