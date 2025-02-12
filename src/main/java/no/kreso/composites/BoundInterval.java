package no.kreso.composites;


import no.kreso.interval.Interval;
import no.kreso.operations.Bound;

import java.time.LocalDate;
import java.util.Comparator;

public class BoundInterval<T> {

    private static final Bound<Integer> integerOperations = new Bound<>(Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    private static final Bound<LocalDate> dateOperations = new Bound<>(Comparator.naturalOrder(), LocalDate.MIN, LocalDate.MAX);

    private final Interval<T> interval;
    private final Bound<T> operations;

    public BoundInterval(T start, T end, Bound<T> operations) {
        this.operations = operations;
        this.interval = operations.validate(start, end);
    }

    public static BoundInterval<Integer> of(Integer start, Integer end) {
        return new BoundInterval<>(start, end, integerOperations);
    }

    public static BoundInterval<LocalDate> of(LocalDate start, LocalDate end) {
        return new BoundInterval<>(start, end, dateOperations) { };
    }

    public T start() {
        return interval.start();
    }

    public T end() {
        return interval.end();
    }

    public boolean subsetOf(BoundInterval<T> other) {
        return operations.subsetOf(this.interval, other.interval);
    }

    public boolean isEmpty() {
        return operations.isEmpty(this.interval);
    }

    public BoundInterval<T> intersection(BoundInterval<T> other) {
        Interval<T> result = operations.intersection(this.interval, other.interval);
        return new BoundInterval<>(result.start(), result.end(), this.operations);
    }

    public BoundInterval<T> union(BoundInterval<T> other) {
        Interval<T> result = operations.union(this.interval, other.interval);
        return new BoundInterval<>(result.start(), result.end(), this.operations);
    }
}
