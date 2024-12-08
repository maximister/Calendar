package ru.mirea.maximister.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParticipantDto {
    long uid;
    String name;
    String surname;
    String email;
    String participationFormat;
    boolean isFree;
}