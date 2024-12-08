package ru.mirea.maximister.authy.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.mirea.maximister.authy.model.User;
import ru.mirea.maximister.authy.model.UserProfile;
import ru.mirea.maximister.authy.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Log4j2
public class SeedConfiguration implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .passwordHash(passwordEncoder.encode("admin"))
                    .build();

            admin.setUserProfile(UserProfile.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .phoneNumber("123445")
                    .build());
            userRepository.save(admin);
            log.debug("created main ADMIN user - {}", admin);
        }
    }
}