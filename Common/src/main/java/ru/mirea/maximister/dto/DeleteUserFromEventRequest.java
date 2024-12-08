package ru.mirea.maximister.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class DeleteUserFromEventRequest {
    String eventId;
    long uid;
    long initiatorUid;
}
