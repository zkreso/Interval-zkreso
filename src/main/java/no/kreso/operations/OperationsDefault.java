package no.kreso.operations;

import no.kreso.interval.Interval;
import no.kreso.interval.IntervalDefault;

import java.util.Comparator;

/**
 * Default implementation of Operations. Relies on a null safe comparator to determine how to interpret null at the
 * lower and upper bound.
 */
public class OperationsDefault<T> implements Operations<T> {

    private final NullSafeComparator<T> comparator;

    public OperationsDefault(NullSafeComparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Create operations on intervals that treat null as negative infinity for the lower bound and
     * as positive infinity for the upper bound. Type U must implement Comparable.
     */
    public static <U extends Comparable<? super U>> Operations<U> unbound() {
        NullSafeComparator<U> comparator = new NullSafeComparator<>(Comparator.<U>naturalOrder(),
                NullSafeComparator.NullInterpretation.NEGATIVE_INFINITY,
                NullSafeComparator.NullInterpretation.POSITIVE_INFINITY);
        return new OperationsDefault<>(comparator);
    }

    /**
     * Create operations on intervals that treat null as positive infinity when applied both to lower
     * and upper bound.
     * Type U must implement Comparable.
     */
    public static <U extends Comparable<? super U>> Operations<U> leftBound() {
        NullSafeComparator<U> comparator = new NullSafeComparator<>(Comparator.<U>naturalOrder(),
                NullSafeComparator.NullInterpretation.POSITIVE_INFINITY,
                NullSafeComparator.NullInterpretation.POSITIVE_INFINITY);
        return new OperationsDefault<>(comparator);
    }

    /**
     * Create operations on intervals that treat null as negative infinity when applied both to lower
     * and upper bound.
     * Type U must implement Comparable.
     */
    public static <U extends Comparable<? super U>> Operations<U> rightBound() {
        NullSafeComparator<U> comparator = new NullSafeComparator<>(Comparator.<U>naturalOrder(),
                NullSafeComparator.NullInterpretation.NEGATIVE_INFINITY,
                NullSafeComparator.NullInterpretation.NEGATIVE_INFINITY);
        return new OperationsDefault<>(comparator);
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
        return comparator.compareStartToEnd(interval.start(), interval.end()) >= 0;
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
        boolean rightStartsAfterLeftEnds = comparator.compareStartToEnd(right.start(), left.end()) > 0;
        if (rightStartsAfterLeftEnds) {
            return validate(left.end(), left.end());
        }
        boolean leftStartsAfterRightEnds = comparator.compareStartToEnd(left.start(), right.end()) > 0;
        if (leftStartsAfterRightEnds) {
            return validate(left.start(), left.start());
        }
        return validate(
                minStart(left.start(), right.start()),
                maxEnd(left.end(), right.end())
        );
    }

    @Override
    public Interval<T> validate(T start, T end) {
        return IntervalDefault.of(start, comparator.compareStartToEnd(start, end) > 0 ? start : end);
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

    private int compareStart(T fst, T snd) {
        return comparator.compareStartToStart(fst, snd);
    }

    private int compareEnd(T fst, T snd) {
        return comparator.compareEndToEnd(fst, snd);
    }
}
