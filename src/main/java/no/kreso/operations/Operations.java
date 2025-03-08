package no.kreso.operations;


import no.kreso.interval.Interval;

/**
 * Interface representing some basic operations on intervals. This interface is intended for implementations that do not
 * throw an Exception on invalid inputs. Instead, invalid inputs are expected to be treated as the empty interval, and
 * invalid outputs should return an empty interval.
 */
public interface Operations<T> {

    /**
     * Will return true if the left interval is a subset of the right interval. Note that by the definition of subset:
     * <ul>
     *     <li>Every set is a subset of itself.</li>
     *     <li>The empty set is a subset of all other sets.</li>
     * </ul>
     */
    boolean subsetOf(Interval<T> left, Interval<T> right);

    /**
     * Will return true if the interval is empty. Note that "negative" intervals (that is, those with an upper bound
     * lesser than the lower bound are also considered empty.
     */
    boolean isEmpty(Interval<T> interval);

    /**
     * Will return the intersection between the left and the right interval.
     */
    Interval<T> intersection(Interval<T> left, Interval<T> right);

    /**
     * Will return the union of the left and the right interval. Note that the intended behavior when taking the union
     * of disjoint intervals is to return the empty interval. This is not consistent with the mathematical operation,
     * but the return type of this method can not inherently represent the result of such unions.
     */
    Interval<T> union(Interval<T> left, Interval<T> right);

    /**
     * This interface assumes that invalid inputs and results will be turned into the empty set instead of throwing an
     * Exception. This method sanitizes the results to achieve that. While such a method need not be publicly exposed,
     * it is done so here for testing purposes.
     */
    Interval<T> validate(T start, T end);
}
