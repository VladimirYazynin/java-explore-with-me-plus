package ru.practicum.ewm.event;

import java.util.Arrays;
import java.util.List;

public enum State {
    PENDING, PUBLISHED, CANCELED, CONFIRMED;

    public static List<State> of(String[] states) {
        return Arrays.stream(states).map(State::valueOf).toList();
    }
}