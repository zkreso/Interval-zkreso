package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class IntegerIntervalTest {

    public static final ComparableInterval<Integer> intervalFactory = ComparableInterval.forType(
            Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE
    );

    @Test
    void creation() {
        Interval<Integer> interval = intervalFactory.of(5, 10);
        assertEquals(5, interval.start());
        assertEquals(10, interval.end());
    }

    @Test
    void creationWithNull() {
        Interval<Integer> interval;

        interval = intervalFactory.of(null, 10);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(10, interval.end());

        interval = intervalFactory.of(5, null);
        assertEquals(5, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());

        interval = intervalFactory.of(null, null);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());
    }

    @Test
    void creationWithReversedParameters() {
        Interval<Integer> interval = intervalFactory.of(10, 5);
        assertTrue(interval.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<Integer> emptySet;

        Interval<Integer> interval = intervalFactory.of(5, 10);
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

        interval = intervalFactory.of(5, 10);
        other = intervalFactory.of(5, 20);
        union = interval.union(other);
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = intervalFactory.of(5, 10);
        other = intervalFactory.of(11, 20);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> intersection;

        interval = intervalFactory.of(5, 11);
        other = intervalFactory.of(10, 20);
        intersection = interval.intersection(other);
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        // Intersections with the empty set should return the empty set
        interval = intervalFactory.of(5, 10);
        other = intervalFactory.of(5, 5);
        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}