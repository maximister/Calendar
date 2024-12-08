package ru.mirea.maximister.authy.util;

import lombok.experimental.UtilityClass;
import ru.mirea.maximister.dto.UserDto;
import ru.mirea.maximister.authy.model.User;

@UtilityClass
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .name(user.getUserProfile().getFirstName())
                .surname(user.getUserProfile().getLastName())
                .email(user.getEmail())
                .build();
    }
}
