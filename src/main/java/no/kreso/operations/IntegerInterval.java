package no.kreso.operations;

import no.kreso.interval.Interval;
import no.kreso.interval.IntervalDefault;

public class IntegerInterval implements Operations<Integer> {

    private final NullSafeComparator comparator;

    public IntegerInterval(NullSafeComparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean subsetOf(Interval<Integer> left, Interval<Integer> right) {
        if (isEmpty(left)) {
            return true;
        }
        if (isEmpty(right)) {
            return false;
        }
        return compareStart(left.start(), right.start()) >= 0 && compareEnd(left.end(), right.end()) <= 0;
    }

    @Override
    public boolean isEmpty(Interval<Integer> interval) {
        return comparator.compareStartToEnd(interval.start(), interval.end()) >= 0;
    }

    @Override
    public Interval<Integer> intersection(Interval<Integer> left, Interval<Integer> right) {
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
    public Interval<Integer> union(Interval<Integer> left, Interval<Integer> right) {
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
    public Interval<Integer> validate(Integer start, Integer end) {
        return IntervalDefault.of(start, comparator.compareStartToEnd(start, end) > 0 ? start : end);
    }

    private Integer minStart(Integer fst, Integer snd) {
        return compareStart(fst, snd) < 0 ? fst : snd;
    }

    private Integer minEnd(Integer fst, Integer snd) {
        return compareEnd(fst, snd) < 0 ? fst : snd;
    }

    private Integer maxStart(Integer fst, Integer snd) {
        return compareStart(fst, snd) > 0 ? fst : snd;
    }

    private Integer maxEnd(Integer fst, Integer snd) {
        return compareEnd(fst, snd) > 0 ? fst : snd;
    }

    private int compareStart(Integer fst, Integer snd) {
        return comparator.compareStartToStart(fst, snd);
    }

    private int compareEnd(Integer fst, Integer snd) {
        return comparator.compareEndToEnd(fst, snd);
    }
}
