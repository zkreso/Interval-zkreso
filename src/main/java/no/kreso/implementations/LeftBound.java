package no.kreso.implementations;

import no.kreso.Interval;

import java.util.Comparator;

public class LeftBound<T> implements IntervalOperations<T> {

    private final Comparator<T> comparator;

    public LeftBound(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean subsetOf(Interval<T> left, Interval<T> right) {
        if (isEmpty(left)) {
            return true;
        }
        if (isEmpty(right)) {
            return isEmpty(left);
        }
        return compareTo(left.start(), right.start()) >= 0 && compareTo(left.end(), right.end()) <= 0;
    }

    @Override
    public boolean isEmpty(Interval<T> interval) {
        return compareTo(interval.start(), interval.end()) >= 0;
    }

    @Override
    public Interval<T> intersection(Interval<T> left, Interval<T> right) {
        if (isEmpty(left)) {
            return left;
        }
        if (isEmpty(right)) {
            return right;
        }
        return validate(
                max(left.start(), right.start()),
                min(left.end(), right.end())
        );
    }

    @Override
    public Interval<T> union(Interval<T> left, Interval<T> right) {
        if (isEmpty(left)) {
            return right;
        }
        if (isEmpty(right)) {
            return left;
        }
        boolean leftEndsBeforeRightStarts = left.end() != null && compareTo(left.end(), right.start()) < 0;
        if (leftEndsBeforeRightStarts) {
            return Interval.of(left.end(), left.end());
        }
        boolean rightEndsBeforeLeftBegins = right.end() != null && compareTo(right.end(), left.start()) < 0;
        if (rightEndsBeforeLeftBegins) {
            return Interval.of(right.end(), right.end());
        }
        return validate(
                min(left.start(), right.start()),
                max(left.end(), right.end())
        );
    }

    @Override
    public Interval<T> validate(T start, T end) {
        return Interval.of(start, max(start, end));
    }

    private T max(T fst, T snd) {
        return compareTo(fst, snd) > 0 ? fst : snd;
    }

    private T min(T fst, T snd) {
        return compareTo(fst, snd) < 0 ? fst : snd;
    }

    private int compareTo(T fst, T snd) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return (fst == null) ? 1 : -1;
        }
        return comparator.compare(fst, snd);
    }
}
