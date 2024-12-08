package ru.mirea.maximister.authy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.maximister.authy.service.UserService;
import ru.mirea.maximister.authy.util.UserMapper;

@RestController
@RequestMapping("/api/v2/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{uid}")
    public ResponseEntity<?> getUser(@PathVariable long uid) {
        return new ResponseEntity<>(
                UserMapper.toDto(userService.getUserByUid(uid)),
                HttpStatus.OK
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return new ResponseEntity<>(
                UserMapper.toDto(userService.getUserByEmail(email)),
                HttpStatus.OK
        );
    }
}
