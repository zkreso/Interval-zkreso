package no.kreso;

import no.kreso.implementations.IntervalInterface;

public record IntervalRecord<T>(T start, T end) implements IntervalInterface<T> {
    public static <T> IntervalRecord<T> of(T start, T end) {
        return new IntervalRecord<>(start, end);
    }
}
