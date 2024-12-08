package ru.mirea.maximister.eventmanagmentservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.maximister.client.UserClient;
import ru.mirea.maximister.eventmanagmentservice.dao.EventDao;
import ru.mirea.maximister.eventmanagmentservice.service.EventService;
import ru.mirea.maximister.eventmanagmentservice.service.EventServiceImpl;
import ru.mirea.maximister.eventmanagmentservice.service.KafkaProducerService;

@Configuration
public class EventServiceConfiguration {
    @Bean
    public EventService eventService(
            EventDao eventDao,
            UserClient userClient,
            KafkaProducerService kafkaProducerService
    ) {
        return new EventServiceImpl(eventDao, userClient, kafkaProducerService);
    }
}
