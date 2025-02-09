package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;


public class ReverseIntegerIntervalTest {

    public static final ComparableInterval<Integer> intervalFactory = ComparableInterval.forType(
            Comparator.reverseOrder(), Integer.MAX_VALUE, Integer.MIN_VALUE
    );

    @Test
    void creation() {
        Interval<Integer> interval = intervalFactory.of(10, 5);
        assertEquals(10, interval.start());
        assertEquals(5, interval.end());
    }

    @Test
    void creationWithNull() {
        Interval<Integer> interval;

        interval = intervalFactory.of(null, 10);
        assertEquals(Integer.MAX_VALUE, interval.start());
        assertEquals(10, interval.end());

        interval = intervalFactory.of(5, null);
        assertEquals(5, interval.start());
        assertEquals(Integer.MIN_VALUE, interval.end());

        interval = intervalFactory.of(null, null);
        assertEquals(Integer.MAX_VALUE, interval.start());
        assertEquals(Integer.MIN_VALUE, interval.end());
    }

    @Test
    void creationWithReversedParameters() {
        Interval<Integer> interval = intervalFactory.of(5, 10);
        assertTrue(interval.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<Integer> emptySet;

        Interval<Integer> interval = intervalFactory.of(10, 5);
        emptySet = intervalFactory.of(20, 20);

        // A set is always its own subset
        assertTrue(interval.subsetOf(interval));

        // The empty set is a subset of all other sets
        Interval<Integer> otherEmptySet = intervalFactory.of(10, 10);
        assertTrue(emptySet.subsetOf(interval));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = intervalFactory.of(10, 10);
        assertFalse(interval.subsetOf(emptySet));
    }

    @Test
    void union() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> union;

        interval = intervalFactory.of(10, 5);
        other = intervalFactory.of(20, 5);
        union = interval.union(other);
        assertEquals(20, union.start());
        assertEquals(5, union.end());

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = intervalFactory.of(10, 5);
        other = intervalFactory.of(20, 11);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> intersection;

        interval = intervalFactory.of(11, 5);
        other = intervalFactory.of(20, 10);
        intersection = interval.intersection(other);
        assertEquals(11, intersection.start());
        assertEquals(10, intersection.end());

        // Intersections with the empty set should return the empty set
        interval = intervalFactory.of(5, 10);
        other = intervalFactory.of(5, 5);
        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}
