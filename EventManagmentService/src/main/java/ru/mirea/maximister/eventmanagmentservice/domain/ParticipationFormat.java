package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipationFormat {
    ONLINE("online"),
    OFFLINE("offline"),
    REJECT("reject");

    private final String name;
}
