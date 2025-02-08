package no.kreso;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;
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
    private final Comparator<T> comparator = Comparator.naturalOrder();
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

    int compareTo(T a, T b) {
        return comparator.compare(a, b);
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

    @Override
    public Iterator<T> iterator() {
        return Stream.iterate(start, (next) -> compareTo(end, next) > 0, this::successor).iterator();
    }
}
