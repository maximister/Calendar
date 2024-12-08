package ru.mirea.maximister.notificator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.maximister.client.EventClient;
import ru.mirea.maximister.client.UserClient;

@Configuration
public class ClientConfig {
    @Bean
    public UserClient userClient() {
        return new UserClient();
    }

    @Bean
    public EventClient eventClient() {
        return new EventClient();
    }
}