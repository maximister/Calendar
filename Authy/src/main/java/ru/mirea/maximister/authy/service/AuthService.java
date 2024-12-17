package ru.mirea.maximister.authy.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mirea.maximister.dto.auth.AuthResponse;
import ru.mirea.maximister.dto.auth.SignInRequest;
import ru.mirea.maximister.dto.auth.SignUpRequest;
import ru.mirea.maximister.authy.model.User;
import ru.mirea.maximister.authy.model.UserProfile;
import ru.mirea.maximister.authy.repository.UserProfileRepository;
import ru.mirea.maximister.authy.repository.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        User user = User.builder()
                .email(request.email())
                .passwordHash(encoder.encode(request.password()))
                .username(request.username())
                .build();

        UserProfile profile = UserProfile.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .build();

        user.setUserProfile(profile);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            log.info("User {} is already registered", request.email());
            throw new IllegalArgumentException();
        }

        userRepository.save(user);
        log.info("User {} was signed up", request.email());

        var jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }

    @Transactional
    public AuthResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        User user = userRepository.findByUsername(request.username()).orElseThrow(
                IllegalArgumentException::new
        );

        var jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }

    public Boolean validate(String token) {
        return jwtService.isTokenValid(token);
    }
}