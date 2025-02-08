package no.kreso;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Base class that can be extended to create an implementation of Interval for types T that already implement
 * Comparable. This implementation does not handle negative and positive infinity. It depends on the existence of a
 * minimal and maximal representable value for the type of element the interval is for. Additionally, the implementation
 * assumes that the lower bound is inclusive, while the upper bound is exclusive. Furthermore, the implementation
 * assumes that the type of element consists of discrete values, and asks for a successor function to provide an
 * implementation of the Iterable interface.
 * @param <T> The type of element of the interval. Must implement Comparable.
 */
public abstract class AbstractInterval<T extends Comparable<? super T>> implements Interval<T>, Iterable<T> {

    private final T start;
    private final T end;

    AbstractInterval(T start, T end) {
        start = start == null ? minvalue() : start;
        end = end == null ? maxValue() : end;
        if (start.compareTo(end) > 0) {
            end = start;
        }
        this.start = start;
        this.end = end;
    }

    /**
     * The minimum possible value of type T.
     */
    abstract T minvalue();

    /**
     * The maximum possible value of type T.
     */
    abstract T maxValue();

    /**
     * Must return the element succeeding the specified element.
     */
    abstract T successor(T current);

    /**
     * A method for creating a new instance of the interval. It is needed for the abstract class to be able to create
     * new instances of the subclass on demand.
     */
    abstract Interval<T> create(T start, T end);

    public T start() {
        return start;
    }

    public T end() {
        return end;
    }

    @Override
    public boolean subsetOf(Interval<T> other) {
        // The empty set is a subset of all other sets
        if (this.isEmpty()) {
            return true;
        }
        // The empty set has only itself as a subset
        if (other.isEmpty()) {
            return false;
        }
        return start.compareTo(other.start()) >= 0 && end.compareTo(other.end()) <= 0;
    }

    @Override
    public Interval<T> intersection(Interval<T> other) {
        return create(
                max(this.start, other.start()),
                min(this.end, other.end())
        );
    }

    /**
     * Returns the union between to intervals. In case of disjoint intervals, returns an empty interval.
     */
    @Override
    public Interval<T> union(Interval<T> other) {
        if (this.end().compareTo(other.start()) < 0 || this.start().compareTo(other.end()) > 0) {
            // Disjoint ranges, return the empty range
            return create(this.start, this.start);
        }
        return create(
                min(this.start, other.start()),
                max(this.end, other.end())
        );
    }

    private T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    private T min(T a, T b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    @Override
    public boolean isEmpty() {
        return start.compareTo(end) == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return Stream.iterate(start, (next) -> end.compareTo(next) > 0, this::successor).iterator();
    }
}
