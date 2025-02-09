package no.kreso;

import java.util.function.BiFunction;

/**
 * Base class that can be extended to create an implementation of Interval.
 * <ul>
 *  <li>
 *      This implementation treats the lower bound as inclusive and upper bound as exclusive.
 *  </li>
 *  <li>
 *      Null arguments for lower bound will be replaced by a minimum value. Likewise for upper
 *      bound and a maximum value. This is necessary because the comparison function does not
 *      have context of whether we are comparing an upper or lower bound.
 *  </li>
 *  <li>
 *      This implementation always returns the empty range when union is applied to disjoint ranges.
 *  </li>
 * </ul>
 * @param <T> The type of element of the interval.
 */
public abstract class AbstractInterval<T> implements Interval<T> {

    private final T start;
    private final T end;
    private final BiFunction<T, T, AbstractInterval<T>> constructor;

    AbstractInterval(T start, T end, BiFunction<T, T, AbstractInterval<T>> constructor) {
        this.constructor = constructor;
        start = start == null ? minvalue() : start;
        end = end == null ? maxValue() : end;
        if (compareTo(start, end) > 0) {
            end = start;
        }
        this.start = start;
        this.end = end;
    }

    protected AbstractInterval<T> newInstance(T start, T end) {
        return constructor.apply(start, end);
    }

    /**
     * The minimum possible value of type T. Used to replace null arguments for lower bound.
     */
    abstract T minvalue();

    /**
     * The maximum possible value of type T. Used to replace null arguments for upper bound.
     */
    abstract T maxValue();

    /**
     * A comparison function. Same requirements as Comparator interface.
     */
    abstract int compareTo(T a, T b);

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
        return compareTo(start, other.start()) >= 0 && compareTo(end, other.end()) <= 0;
    }

    @Override
    final public Interval<T> intersection(Interval<T> other) {
        return newInstance(
                max(this.start, other.start()),
                min(this.end, other.end())
        );
    }

    /**
     * Returns the union between to intervals. In case of disjoint intervals, returns an empty interval.
     */
    @Override
    final public Interval<T> union(Interval<T> other) {
        if (compareTo(this.end, other.start()) < 0 || compareTo(this.start, other.end()) > 0) {
            // Disjoint ranges, return the empty range
            return newInstance(this.start, this.start);
        }
        return newInstance(
                min(this.start, other.start()),
                max(this.end, other.end())
        );
    }

    private T max(T a, T b) {
        return compareTo(a, b) > 0 ? a : b;
    }

    private T min(T a, T b) {
        return compareTo(a, b) < 0 ? a : b;
    }

    @Override
    final public boolean isEmpty() {
        return compareTo(start, end) == 0;
    }
}
