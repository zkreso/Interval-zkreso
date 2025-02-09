package no.kreso;

/**
 * Interface representing basic operations on intervals.
 * @param <T> The type of element included in the interval. The elements should be comparable, but since there could be
 *           multiple ways to compare the type of element, we do not enforce that the type should implement Comparable,
 *           and leave it up to the implementation.
 */
public interface Interval<T> {

    /**
     * The lower bound of the interval. It is up to the implementer if it should be inclusive or exclusive.
     */
    T start();

    /**
     * The upper bound of the interval. It is up to the implementer if it should be inclusive or exclusive.
     */
    T end();

    /**
     * Returns true if the elements of the interval are a subset of the specified interval. In other words, the
     * specified interval must include the interval calling the method. Note that the definition of a subset implies
     * that all sets also have themselves as a subset. In other words, this method will also return true when the two
     * intervals are equal.
     */
    boolean subsetOf(Interval<T> other);

    /**
     * Returns true if the interval is empty. In most interpretations this means that the lower and upper bound are
     * equal.
     */
    boolean isEmpty();

    /**
     * Returns the intersection of two intervals.
     */
    Interval<T> intersection(Interval<T> other);

    /**
     * Returns the union of two intervals. It is up to the implementation how to handle the union of two disjoint
     * intervals. However, this interface does not define any methods for getting multiple lower or upper bounds for
     * an interval, so the most practical approach is to return the empty interval in such cases.
     */
    Interval<T> union(Interval<T> other);
}
