package no.kreso.implementations;

import no.kreso.IntervalRecord;

public interface IntervalOperations<T> {
    boolean subsetOf(IntervalInterface<T> left, IntervalInterface<T> right);
    boolean isEmpty(IntervalInterface<T> interval);
    IntervalInterface<T> intersection(IntervalInterface<T> left, IntervalInterface<T> right);
    IntervalInterface<T> union(IntervalInterface<T> left, IntervalInterface<T> right);
}
