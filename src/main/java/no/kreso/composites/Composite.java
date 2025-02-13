package no.kreso.composites;


import no.kreso.interval.Interval;
import no.kreso.operations.Operations;


public abstract class Composite<T, U extends Composite<T, U>> implements Interval<T> {

    private final Interval<T> interval;
    private final Operations<T> operations;

    public Composite(T start, T end, Operations<T> operations) {
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
