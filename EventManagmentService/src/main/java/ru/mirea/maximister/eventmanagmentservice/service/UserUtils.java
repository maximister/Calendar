package ru.mirea.maximister.eventmanagmentservice.service;

import lombok.experimental.UtilityClass;
import ru.mirea.maximister.eventmanagmentservice.domain.Event;
import ru.mirea.maximister.eventmanagmentservice.domain.OpennessPolicy;

@UtilityClass
public class UserUtils {
    public static boolean isUserAllowedToChangeEvent(long uid, Event event) {
        return Long.compare(uid, event.getAuthorUid()) == 0 ||
                event.getOpennessPolicy() == OpennessPolicy.OPENED;
    }
}
