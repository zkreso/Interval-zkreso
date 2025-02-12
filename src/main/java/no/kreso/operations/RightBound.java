package no.kreso.operations;

import no.kreso.interval.Interval;
import no.kreso.interval.IntervalDefault;

import java.util.Comparator;

public class RightBound<T> implements Operations<T> {

    private final Comparator<T> comparator;

    public RightBound(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean subsetOf(Interval<T> left, Interval<T> right) {
        if (isEmpty(left)) {
            return true;
        }
        if (isEmpty(right)) {
            return false;
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
        boolean leftEndsBeforeRightStarts = compareTo(left.end(), right.start()) < 0;
        if (leftEndsBeforeRightStarts) {
            return IntervalDefault.of(right.start(), right.start());
        }
        boolean rightEndsBeforeLeftBegins = compareTo(right.end(), left.start()) < 0;
        if (rightEndsBeforeLeftBegins) {
            return IntervalDefault.of(left.start(), left.start());
        }
        return validate(
                min(left.start(), right.start()),
                max(left.end(), right.end())
        );
    }

    @Override
    public Interval<T> validate(T start, T end) {
        return IntervalDefault.of(start, max(start, end));
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
            return (fst == null) ? -1 : 1;
        }
        return comparator.compare(fst, snd);
    }
}
