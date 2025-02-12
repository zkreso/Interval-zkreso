package no.kreso.operations;

import no.kreso.interval.Interval;
import no.kreso.interval.IntervalDefault;

import java.util.Comparator;

public class Unbound<T> implements Operations<T> {

    private final Comparator<T> comparator;

    public Unbound(Comparator<T> comparator) {
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
        return compareStart(left.start(), right.start()) >= 0 && compareEnd(left.end(), right.end()) <= 0;
    }

    @Override
    public boolean isEmpty(Interval<T> interval) {
        if (interval.start() == null || interval.end() == null) {
            return false;
        }
        return comparator.compare(interval.start(), interval.end()) >= 0;
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
                maxStart(left.start(), right.start()),
                minEnd(left.end(), right.end())
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
        boolean leftEndsBeforeRightStarts = left.end() != null && compareStart(left.end(), right.start()) < 0;
        if (leftEndsBeforeRightStarts) {
            return validate(left.end(), left.end());
        }
        boolean rightEndsBeforeLeftStarts = left.start() != null && compareEnd(right.end(), left.start()) < 0;
        if (rightEndsBeforeLeftStarts) {
            return validate(left.start(), left.start());
        }
        return validate(
                minStart(left.start(), right.start()),
                maxEnd(left.end(), right.end())
        );
    }

    @Override
    public Interval<T> validate(T start, T end) {
        if (start == null || end == null) {
            return IntervalDefault.of(start, end);
        }
        return IntervalDefault.of(start, comparator.compare(start, end) > 0 ? start : end);
    }

    private T minStart(T fst, T snd) {
        return compareStart(fst, snd) < 0 ? fst : snd;
    }

    private T minEnd(T fst, T snd) {
        return compareEnd(fst, snd) < 0 ? fst : snd;
    }

    private T maxStart(T fst, T snd) {
        return compareStart(fst, snd) > 0 ? fst : snd;
    }

    private T maxEnd(T fst, T snd) {
        return compareEnd(fst, snd) > 0 ? fst : snd;
    }

    private int compareEnd(T fst, T snd) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return (fst == null) ? 1 : -1;
        }
        return comparator.compare(fst, snd);
    }

    private int compareStart(T fst, T snd) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return (fst == null) ? -1 : 1;
        }
        return comparator.compare(fst, snd);
    }
}
