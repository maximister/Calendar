package ru.mirea.maximister.authy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.maximister.dto.auth.SignInRequest;
import ru.mirea.maximister.dto.auth.SignUpRequest;
import ru.mirea.maximister.authy.service.AuthService;

@RestController
@RequestMapping("/api/v2/auth")
@AllArgsConstructor
public class AuthorizationController {
    private final AuthService authService;

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
