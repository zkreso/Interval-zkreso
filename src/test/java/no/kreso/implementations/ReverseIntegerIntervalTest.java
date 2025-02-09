package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import static no.kreso.implementations.Intervals.*;
import static org.junit.jupiter.api.Assertions.*;


public class ReverseIntegerIntervalTest {

    @Test
    void creation() {
        Interval<Integer> interval = REVERSE_INTEGER_INTERVAL.of(10, 5);
        assertEquals(10, interval.start());
        assertEquals(5, interval.end());
    }

    @Test
    void creationWithNull() {
        Interval<Integer> interval;

        interval = REVERSE_INTEGER_INTERVAL.of(null, 10);
        assertEquals(Integer.MAX_VALUE, interval.start());
        assertEquals(10, interval.end());

        interval = REVERSE_INTEGER_INTERVAL.of(5, null);
        assertEquals(5, interval.start());
        assertEquals(Integer.MIN_VALUE, interval.end());

        interval = REVERSE_INTEGER_INTERVAL.of(null, null);
        assertEquals(Integer.MAX_VALUE, interval.start());
        assertEquals(Integer.MIN_VALUE, interval.end());
    }

    @Test
    void creationWithReversedParameters() {
        Interval<Integer> interval = REVERSE_INTEGER_INTERVAL.of(5, 10);
        assertTrue(interval.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<Integer> emptySet;

        Interval<Integer> interval = REVERSE_INTEGER_INTERVAL.of(10, 5);
        emptySet = REVERSE_INTEGER_INTERVAL.of(20, 20);

        // A set is always its own subset
        assertTrue(interval.subsetOf(interval));

        // The empty set is a subset of all other sets
        Interval<Integer> otherEmptySet = REVERSE_INTEGER_INTERVAL.of(10, 10);
        assertTrue(emptySet.subsetOf(interval));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = REVERSE_INTEGER_INTERVAL.of(10, 10);
        assertFalse(interval.subsetOf(emptySet));
    }

    @Test
    void union() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> union;

        interval = REVERSE_INTEGER_INTERVAL.of(10, 5);
        other = REVERSE_INTEGER_INTERVAL.of(20, 5);
        union = interval.union(other);
        assertEquals(20, union.start());
        assertEquals(5, union.end());

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = REVERSE_INTEGER_INTERVAL.of(10, 5);
        other = REVERSE_INTEGER_INTERVAL.of(20, 11);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> intersection;

        interval = REVERSE_INTEGER_INTERVAL.of(11, 5);
        other = REVERSE_INTEGER_INTERVAL.of(20, 10);
        intersection = interval.intersection(other);
        assertEquals(11, intersection.start());
        assertEquals(10, intersection.end());

        // Intersections with the empty set should return the empty set
        interval = REVERSE_INTEGER_INTERVAL.of(5, 10);
        other = REVERSE_INTEGER_INTERVAL.of(5, 5);
        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}
