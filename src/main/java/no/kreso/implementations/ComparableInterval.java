package no.kreso.implementations;

import no.kreso.Interval;

import java.util.Comparator;

/**
 * Base class that can be extended to create an implementation of Interval for types that are comparable.
 * We do not enforce that the type must extend Comparable, because we want to allow non-default comparators as well.
 * Instead, the comparator must be supplied in the constructor. Once constructed, the of() method can be used to create
 * new instances with a copy of the existing configuration.
 * <ul>
 *  <li>
 *      This implementation treats the lower bound as inclusive and upper bound as exclusive.
 *  </li>
 *  <li>
 *      Null arguments for lower bound will be replaced by a minimum value. Likewise for upper
 *      bound and a maximum value. This is necessary because the Comparator interface does not
 *      handle null values.
 *  </li>
 *  <li>
 *      This implementation always returns the empty interval when union is applied to disjoint intervals.
 *  </li>
 * </ul>
 * @param <T> The type of element of the interval.
 */
public class ComparableInterval<T> implements Interval<T> {

    private final T start;
    private final T end;
    private final Comparator<T> comparator;
    private final T minValue;
    private final T maxValue;

    public static <T> ComparableInterval<T> forType(Comparator<T> comparator, T minValue, T maxValue) {
        return new ComparableInterval<>(comparator, minValue, maxValue, minValue, minValue);
    }

    public Interval<T> of(T start, T end) {
        return new ComparableInterval<>(this.comparator, this.minValue, this.maxValue, start, end);
    }

    private ComparableInterval(Comparator<T> comparator, T minValue, T maxValue, T start, T end) {
        this.comparator = comparator;
        this.minValue = minValue;
        this.maxValue = maxValue;
        start = start == null ? minValue : start;
        end = end == null ? maxValue : end;
        if (comparator.compare(start, end) > 0) {
            end = start;
        }
        this.start = start;
        this.end = end;
    }

    public T start() {
        return start;
    }

    public T end() {
        return end;
    }

    @Override
    final public boolean subsetOf(Interval<T> other) {
        // The empty set is a subset of all other sets
        if (this.isEmpty()) {
            return true;
        }
        // The empty set has only itself as a subset
        if (other.isEmpty()) {
            return false;
        }
        return comparator.compare(start, other.start()) >= 0 && comparator.compare(end, other.end()) <= 0;
    }

    @Override
    final public Interval<T> intersection(Interval<T> other) {
        T start1 = max(this.start, other.start());
        return of(start1, min(this.end, other.end()));
    }

    /**
     * Returns the union between to intervals. In case of disjoint intervals, returns an empty interval.
     */
    @Override
    final public Interval<T> union(Interval<T> other) {
        if (comparator.compare(this.end, other.start()) < 0 || comparator.compare(this.start, other.end()) > 0) {
            // Disjoint intervals, return the empty interval
            return of(this.start, this.start);
        }
        T start1 = min(this.start, other.start());
        return of(start1, max(this.end, other.end()));
    }

    private T max(T a, T b) {
        return comparator.compare(a, b) > 0 ? a : b;
    }

    private T min(T a, T b) {
        return comparator.compare(a, b) < 0 ? a : b;
    }

    @Override
    final public boolean isEmpty() {
        return comparator.compare(start, end) == 0;
    }
}
