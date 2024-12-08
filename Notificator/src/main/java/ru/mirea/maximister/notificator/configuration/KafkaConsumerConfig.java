package ru.mirea.maximister.notificator.configuration;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.mirea.maximister.dto.kafka.ChangeEvent;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ChangeEvent> consumerFactory() {
        JsonDeserializer<ChangeEvent> deserializer = new JsonDeserializer<>(ChangeEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificator");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());

        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());

        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ChangeEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChangeEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

