package no.kreso.implementations;

import no.kreso.Interval;

import java.time.LocalDate;
import java.util.Comparator;

public enum Intervals {

    INTEGER_INTERVAL(ComparableInterval.forType(Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE)),
    DATE_INTERVAL(ComparableInterval.forType(Comparator.naturalOrder(), LocalDate.MIN, LocalDate.MAX)),
    REVERSE_INTEGER_INTERVAL(ComparableInterval.forType(Comparator.reverseOrder(), Integer.MAX_VALUE, Integer.MIN_VALUE));

    private final ComparableInterval<?> intervalFactory;

    <T> Intervals(ComparableInterval<T> intervalFactory) {
        this.intervalFactory = intervalFactory;
    }

    <T> Interval<T> of(T start, T end) {
        return ((ComparableInterval<T>) intervalFactory).of(start, end);
    }
}
