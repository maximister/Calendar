package ru.mirea.maximister.authy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mirea.maximister.authy.model.User;
import ru.mirea.maximister.authy.repository.UserRepository;

import java.util.Optional;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() ->
                new UsernameNotFoundException("User " + username + "not found")
        );
    }
}