package no.kreso.implementations;

import no.kreso.Interval;

import java.util.Comparator;

/**
 *  Implementation of a bound interval for any type T. With bound, we mean that it does not handle intervals with
 *  positive or negative infinity as bounds.
 *  <ul>
 *  <li>Because the interval is bound, a min and a max value must be provided.</li>
 *  <li>Because the implementation is generic, a comparator must also be provided.</li>
 *  <li>The interval is initially configured and constructed using the forType() method. Once configured, the actual
 *    intervals can be created using of() with only the lower and upper bound as necessary parameters.</li>
 *  </ul>
 *  Upper bound is treated as exclusive and lower bound as inclusive. Unions between disjoint intervals are not
 *  supported and will return the empty interval.
 */
public class BoundInterval<T> implements Interval<T> {

    private final T start;
    private final T end;
    private final Comparator<T> comparator;
    private final T minValue;
    private final T maxValue;

    public static <T> BoundInterval<T> forType(Comparator<T> comparator, T minValue, T maxValue) {
        return new BoundInterval<>(comparator, minValue, maxValue, minValue, minValue);
    }

    public Interval<T> of(T start, T end) {
        return new BoundInterval<>(this.comparator, this.minValue, this.maxValue, start, end);
    }

    private BoundInterval(Comparator<T> comparator, T minValue, T maxValue, T start, T end) {
        this.comparator = comparator;
        this.minValue = minValue;
        this.maxValue = maxValue;
        start = start == null || comparator.compare(start, minValue) < 0 ? minValue : start;
        end = end == null || comparator.compare(end, maxValue) > 0 ? maxValue : end;
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
        return of(
                max(this.start, other.start()),
                min(this.end, other.end())
        );
    }

    /**
     * Returns the union between to intervals. In case of disjoint intervals, returns an empty interval.
     */
    @Override
    final public Interval<T> union(Interval<T> other) {
        if (this.isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return this;
        }
        if (comparator.compare(this.end, other.start()) < 0 || comparator.compare(this.start, other.end()) > 0) {
            // Disjoint intervals, return the empty interval
            return of(this.start, this.start);
        }
        return of(
                min(this.start, other.start()),
                max(this.end, other.end())
        );
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
