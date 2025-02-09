package no.kreso.implementations;

import no.kreso.Interval;

import java.time.LocalDate;
import java.util.Comparator;

public interface IntervalFactory<T> {
    Interval<T> of(T start, T end);

    IntervalFactory<Integer> INTEGER_INTERVAL = new IntervalFactoryImpl<>(Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    IntervalFactory<LocalDate> DATE_INTERVAL = new IntervalFactoryImpl<>(Comparator.naturalOrder(), LocalDate.MIN, LocalDate.MAX);
    IntervalFactory<Integer> REVERSE_INTEGER_INTERVAL = new IntervalFactoryImpl<>(Comparator.reverseOrder(), Integer.MAX_VALUE, Integer.MIN_VALUE);

    class IntervalFactoryImpl<T> implements IntervalFactory<T> {
        private final ComparableInterval<T> intervalFactory;
        private IntervalFactoryImpl(Comparator<T> comparator, T minValue, T maxValue) {
            this.intervalFactory = ComparableInterval.forType(comparator, minValue, maxValue);
        }
        public Interval<T> of(T start, T end) {
            return intervalFactory.of(start, end);
        }
    }
}
