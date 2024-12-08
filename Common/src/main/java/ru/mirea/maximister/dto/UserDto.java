package ru.mirea.maximister.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    long uid;
    String username;
    String name;
    String surname;
    String email;
}
