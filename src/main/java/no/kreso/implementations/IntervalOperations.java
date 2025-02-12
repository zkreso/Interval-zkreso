package no.kreso.implementations;


import no.kreso.Interval;

public interface IntervalOperations<T> {
    boolean subsetOf(Interval<T> left, Interval<T> right);
    boolean isEmpty(Interval<T> interval);
    Interval<T> intersection(Interval<T> left, Interval<T> right);
    Interval<T> union(Interval<T> left, Interval<T> right);
    Interval<T> validate(T start, T end);
}
