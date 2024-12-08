package ru.mirea.maximister.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AddUserToEventRequest {
    String eventId;
    long uid;
    long initiatorUid;
}
