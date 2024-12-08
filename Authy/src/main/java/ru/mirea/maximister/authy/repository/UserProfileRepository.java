package ru.mirea.maximister.authy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.maximister.authy.model.UserProfile;



public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
