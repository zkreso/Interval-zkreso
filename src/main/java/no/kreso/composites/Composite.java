package no.kreso.composites;


import no.kreso.interval.Interval;
import no.kreso.operations.Operations;


/**
 * Class that acts as a composite of the Interval and Operations (on interval) interfaces. The primary goal of this
 * class is to unify the creation of intervals with operations on them, for a less verbose API. Additionally, we most
 * often want to keep intervals created through different Operations contexts separate. This class acts as a wrapper which
 * enables the type system to enforce this for us automatically.
 *
 * @param <T> The type of the interval
 * @param <U> The concrete implementation of this abstract class.
 */
public abstract class Composite<T, U extends Composite<T, U>> implements Interval<T> {

    private final Interval<T> interval;
    private final Operations<T> operations;

    Composite(T start, T end, Operations<T> operations) {
        this.operations = operations;
        this.interval = operations().validate(start, end);
    }

    final Operations<T> operations() {
        return operations;
    }

    abstract U newInstance(T start, T end);

    final public T start() {
        return interval.start();
    }

    final public T end() {
        return interval.end();
    }

    final public boolean subsetOf(Composite<T, U> other) {
        return operations.subsetOf(this.interval, other.interval);
    }

    final public boolean isEmpty() {
        return operations.isEmpty(this.interval);
    }

    final public U intersection(Composite<T, U> other) {
        Interval<T> result = operations.intersection(this.interval, other.interval);
        return newInstance(result.start(), result.end());
    }

    final public U union(Composite<T, U> other) {
        Interval<T> result = operations.union(this.interval, other.interval);
        return newInstance(result.start(), result.end());
    }
}
