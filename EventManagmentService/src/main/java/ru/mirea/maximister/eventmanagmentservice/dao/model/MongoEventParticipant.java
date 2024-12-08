package ru.mirea.maximister.eventmanagmentservice.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MongoEventParticipant {
     long uid;
     String name;
     String surname;
     String email;
     MongoParticipationFormat participationFormat;
     boolean isFree;
}
