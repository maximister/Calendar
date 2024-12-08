package ru.mirea.maximister.eventmanagmentservice.dao.model;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "events")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MongoEvent {
    @Id
    UUID id;
    long authorUid;
    String title;
    @Nullable
    String description;
    MongoEventInterval interval;
    @Nullable
    String conferenceUrl;
    @Builder.Default
    List<MongoEventParticipant> participants = List.of();
    MongoEventPeriod period;
    MongoOpennessPolicy opennessPolicy;
}