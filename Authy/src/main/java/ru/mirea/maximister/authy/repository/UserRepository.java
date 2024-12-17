package ru.mirea.maximister.authy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.maximister.authy.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUid(Long uid);

    Boolean deleteUserByUid(long uid);
}