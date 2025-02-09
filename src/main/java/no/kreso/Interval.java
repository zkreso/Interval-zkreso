package no.kreso;

/**
 * Interface representing an interval with basic interval operations.
 * @param <T> The type of element included in the interval. The elements should be comparable, but since there could be
 *           multiple ways to compare the type of element in question, we do not enforce that the type should implement
 *           Comparable. Instead, we leave it up to the implementation.
 */
public interface Interval<T> {

    /**
     * The lower bound of the interval. It is up to the implementation to decide if it should be treated as inclusive or
     * exclusive.
     */
    T start();

    /**
     * The upper bound of the interval. It is up to the implementation to decide if it should be treated as inclusive or
     * exclusive.
     */
    T end();

    /**
     * Should return true if the provided interval includes the interval calling the method. Note that from the
     * definition of subset, we get that this method should also return true if the two intervals are equal.
     */
    boolean subsetOf(Interval<T> other);

    /**
     * Should return true if the interval is empty. In most interpretations this means that the lower and upper bound
     * are equal.
     */
    boolean isEmpty();

    /**
     * Should return the intersection of two intervals.
     */
    Interval<T> intersection(Interval<T> other);

    /**
     * Should return the union of two intervals. It is up to the implementation how to handle the union of disjoint
     * intervals. However, this interface does not define any methods for retrieving multiple lower or upper bounds
     * an interval, so the user must expect that the result will most likely be the empty interval.
     */
    Interval<T> union(Interval<T> other);
}
