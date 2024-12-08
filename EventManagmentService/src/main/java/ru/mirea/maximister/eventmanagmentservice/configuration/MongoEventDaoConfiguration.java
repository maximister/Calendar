package ru.mirea.maximister.eventmanagmentservice.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mirea.maximister.eventmanagmentservice.dao.MongoEventDao;
import ru.mirea.maximister.eventmanagmentservice.dao.model.MongoEvent;

@Configuration
public class MongoEventDaoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String mongoDatabaseName;

    @Bean
    public MongoClient mongoClient() {
        CodecRegistry pojoCodecRegistry =
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry =
                CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(mongoUri))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(codecRegistry)
                .build();


        return MongoClients.create(settings);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase(mongoDatabaseName);
    }

    @Bean
    public MongoCollection<MongoEvent> mongoEventCollection(MongoDatabase database) {
        return database.getCollection("events", MongoEvent.class);
    }

    @Bean
    public MongoEventDao mongoEventDao(MongoCollection<MongoEvent> collection) {
        return new MongoEventDao(collection);
    }
}
