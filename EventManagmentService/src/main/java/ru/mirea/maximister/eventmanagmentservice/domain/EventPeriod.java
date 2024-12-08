package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventPeriod {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    NONE("none");

    private final String name;
}
