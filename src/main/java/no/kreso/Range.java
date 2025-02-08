package no.kreso;

public interface Range<T extends Comparable<? super T>> {
    T start();
    T end();
    boolean subsetOf(Range<T> other);
    boolean isEmpty();
    Range<T> intersection(Range<T> other);
    Range<T> union(Range<T> other);
}
