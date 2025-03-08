package no.kreso.operations;

import no.kreso.interval.Interval;
import no.kreso.interval.IntervalDefault;

import java.util.Comparator;

/**
 * Default implementation of Operations. This implementation is null safe. However, to achieve null safety, the user
 * must specify if null should be interpreted as positive or negative infinity at both bounds of the interval.
 */
public class OperationsDefault<T> implements Operations<T> {

    private final Comparator<T> comparator;
    private final NullInterpretation lower;
    private final NullInterpretation upper;

    /**
     * Default constructor. Note that some parameterless static methods are available if the default comparator for
     * the type is sufficient.
     * @param comparator Comparator for the type in question.
     * @param lower How a null value at the lower bound of the interval should be interpreted.
     * @param upper How a null value at the upper bound of the interval should be interpreted.
     */
    public OperationsDefault(
            Comparator<T> comparator,
            NullInterpretation lower,
            NullInterpretation upper
    ) {
        this.comparator = comparator;
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Create operations on intervals that treat null as negative infinity for the lower bound and
     * as positive infinity for the upper bound. The default comparator for the type will be used.
     */
    public static <U extends Comparable<? super U>> Operations<U> unbound() {
        return new OperationsDefault<>(
                Comparator.<U>naturalOrder(),
                NullInterpretation.NEGATIVE_INFINITY,
                NullInterpretation.POSITIVE_INFINITY
        );
    }

    /**
     * Create operations on intervals that treat null as positive infinity when applied both to lower
     * and upper bound. The default comparator for the type will be used.
     */
    public static <U extends Comparable<? super U>> Operations<U> leftBound() {
        return new OperationsDefault<>(
                Comparator.<U>naturalOrder(),
                NullInterpretation.POSITIVE_INFINITY,
                NullInterpretation.POSITIVE_INFINITY
        );
    }

    /**
     * Create operations on intervals that treat null as negative infinity when applied both to lower
     * and upper bound. The default comparator for the type will be used.
     */
    public static <U extends Comparable<? super U>> Operations<U> rightBound() {
        return new OperationsDefault<>(
                Comparator.<U>naturalOrder(),
                NullInterpretation.NEGATIVE_INFINITY,
                NullInterpretation.NEGATIVE_INFINITY
        );
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
        return compareStartToEnd(interval.start(), interval.end()) >= 0;
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
        boolean rightStartsAfterLeftEnds = compareStartToEnd(right.start(), left.end()) > 0;
        if (rightStartsAfterLeftEnds) {
            return validate(left.end(), left.end());
        }
        boolean leftStartsAfterRightEnds = compareStartToEnd(left.start(), right.end()) > 0;
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
        return IntervalDefault.of(start, compareStartToEnd(start, end) > 0 ? start : end);
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
        return compare(fst, snd, lower);
    }

    private int compareEnd(T fst, T snd) {
        return compare(fst, snd, upper);
    }

    private int compareStartToEnd(T start, T end) {
        if (start == null && end == null && lower == upper) {
            return 0;
        }
        if (start == null) {
            return (lower == NullInterpretation.NEGATIVE_INFINITY) ? -1 : 1;
        }
        if (end == null) {
            return (upper == NullInterpretation.POSITIVE_INFINITY) ? -1 : 1;
        }
        return comparator.compare(start, end);
    }

    private int compare(T fst, T snd, NullInterpretation bound) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return fst == null ? bound.signum : -bound.signum;
        }
        return comparator.compare(fst, snd);
    }

    public enum NullInterpretation {

        POSITIVE_INFINITY(1),
        NEGATIVE_INFINITY(-1);

        private final int signum;

        NullInterpretation(int signum) {
            this.signum = signum;
        }
    }
}
