package no.kreso;

public interface Range<T extends Comparable<? super T>> {
    T start();
    T end();
    boolean subsetOf(Range<T> other);
    boolean isEmpty();
    Range<T> and(Range<T> other);
    Range<T> or(Range<T> other);
}
