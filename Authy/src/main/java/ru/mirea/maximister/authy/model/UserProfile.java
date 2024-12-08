package ru.mirea.maximister.authy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String firstName;
    private String lastName;
    private String phoneNumber;
}