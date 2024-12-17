package ru.mirea.maximister.authy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.authy.model.User;
import ru.mirea.maximister.authy.repository.UserProfileRepository;
import ru.mirea.maximister.authy.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUid(long uid) {
        var user = userRepository.findByUid(uid);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return user.get();
    }
    public User getUserByEmail(String email) {
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return user.get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
