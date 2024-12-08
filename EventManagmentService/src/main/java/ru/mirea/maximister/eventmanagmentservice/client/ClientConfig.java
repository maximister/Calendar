package ru.mirea.maximister.eventmanagmentservice.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.maximister.client.UserClient;

@Configuration
public class ClientConfig {
    @Bean
    public UserClient userClient() {
        return new UserClient();
    }
}
