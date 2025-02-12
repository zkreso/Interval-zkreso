package no.kreso.operations;

import no.kreso.interval.Interval;
import no.kreso.interval.IntervalDefault;

import java.util.Comparator;

public final class Bound<T> implements Operations<T> {

    private final Comparator<T> comparator;
    private final T minValue;
    private final T maxValue;

    public Bound(Comparator<T> comparator, T minValue, T maxValue) {
        this.comparator = comparator;
        this.maxValue = maxValue;
        this.minValue = minValue;
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

    public Interval<T> validate(T start, T end) {
        start = sanitizeStart(start);
        end = maxEnd(start, end);
        return IntervalDefault.of(start, end);
    }

    @Override
    public boolean isEmpty(Interval<T> interval) {
        return comparator.compare(sanitizeStart(interval.start()), sanitizeEnd(interval.end())) == 0;
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
        boolean leftEndsBeforeRightStarts = comparator.compare(sanitizeEnd(left.end()), sanitizeStart(right.start())) < 0;
        boolean rightEndsBeforeLeftStarts = comparator.compare(sanitizeEnd(right.end()), sanitizeStart(left.start())) < 0;
        if (leftEndsBeforeRightStarts || rightEndsBeforeLeftStarts) {
            // Disjoint intervals, return the empty interval
            return validate(minValue, minValue);
        }
        return validate(
                minStart(left.start(), right.start()),
                maxEnd(left.end(), right.end())
        );
    }

    private T minStart(T fst, T snd) {
        fst = sanitizeStart(fst);
        snd = sanitizeStart(snd);
        return comparator.compare(fst, snd) < 0 ? fst : snd;
    }

    private T minEnd(T fst, T snd) {
        fst = sanitizeEnd(fst);
        snd = sanitizeEnd(snd);
        return comparator.compare(fst, snd) < 0 ? fst : snd;
    }

    private T maxStart(T fst, T snd) {
        fst = sanitizeStart(fst);
        snd = sanitizeStart(snd);
        return comparator.compare(fst, snd) > 0 ? fst : snd;
    }

    private T maxEnd(T fst, T snd) {
        fst = sanitizeEnd(fst);
        snd = sanitizeEnd(snd);
        return comparator.compare(fst, snd) > 0 ? fst : snd;
    }

    private T sanitizeStart(T start) {
        return start == null ? minValue : start;
    }

    private T sanitizeEnd(T end) {
        return end == null ? maxValue : end;
    }

    private int compareStart(T fst, T snd) {
        return comparator.compare(sanitizeStart(fst), sanitizeStart(snd));
    }

    private int compareEnd(T fst, T snd) {
        return comparator.compare(sanitizeEnd(fst), sanitizeEnd(snd));
    }
}
