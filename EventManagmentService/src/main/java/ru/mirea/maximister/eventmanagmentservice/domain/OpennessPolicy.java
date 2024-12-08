package ru.mirea.maximister.eventmanagmentservice.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OpennessPolicy {
    OPENED("opened"),
    CLOSED("closed");

    private final String name;
}
