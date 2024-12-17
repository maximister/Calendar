package ru.mirea.maximister.authy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.maximister.dto.auth.SignInRequest;
import ru.mirea.maximister.dto.auth.SignUpRequest;
import ru.mirea.maximister.authy.service.AuthService;

@RestController
@RequestMapping("/api/v2/auth")
@AllArgsConstructor
public class AuthorizationController {
    private final AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return new ResponseEntity<>(
                authService.validate(token),
                HttpStatus.OK
        );
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(
                authService.signIn(request),
                HttpStatus.OK
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        return new ResponseEntity<>(
                authService.signUp(request),
                HttpStatus.OK
        );
    }
}
