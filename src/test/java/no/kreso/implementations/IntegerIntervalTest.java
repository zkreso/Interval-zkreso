package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import static no.kreso.implementations.IntervalFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class IntegerIntervalTest {

    @Test
    void creation() {
        Interval<Integer> interval = INTEGER_INTERVAL.of(5, 10);
        assertEquals(5, interval.start());
        assertEquals(10, interval.end());
    }

    @Test
    void creationWithNull() {
        Interval<Integer> interval;

        interval = INTEGER_INTERVAL.of(null, 10);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(10, interval.end());

        interval = INTEGER_INTERVAL.of(5, null);
        assertEquals(5, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());

        interval = INTEGER_INTERVAL.of(null, null);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());
    }

    @Test
    void creationWithReversedParameters() {
        Interval<Integer> interval = INTEGER_INTERVAL.of(10, 5);
        assertTrue(interval.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<Integer> emptySet;

        Interval<Integer> interval = INTEGER_INTERVAL.of(5, 10);
        emptySet = INTEGER_INTERVAL.of(20, 20);

        // A set is always its own subset
        assertTrue(interval.subsetOf(interval));

        // The empty set is a subset of all other sets
        Interval<Integer> otherEmptySet = INTEGER_INTERVAL.of(10, 10);
        assertTrue(emptySet.subsetOf(interval));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = INTEGER_INTERVAL.of(10, 10);
        assertFalse(interval.subsetOf(emptySet));
    }

    @Test
    void union() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> union;

        interval = INTEGER_INTERVAL.of(5, 10);
        other = INTEGER_INTERVAL.of(5, 20);
        union = interval.union(other);
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = INTEGER_INTERVAL.of(5, 10);
        other = INTEGER_INTERVAL.of(11, 20);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<Integer> interval;
        Interval<Integer> other;
        Interval<Integer> intersection;

        interval = INTEGER_INTERVAL.of(5, 11);
        other = INTEGER_INTERVAL.of(10, 20);
        intersection = interval.intersection(other);
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        // Intersections with the empty set should return the empty set
        interval = INTEGER_INTERVAL.of(5, 10);
        other = INTEGER_INTERVAL.of(5, 5);
        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());

        // Make sure end is exclusive
        interval = INTEGER_INTERVAL.of(5, 10);
        other = INTEGER_INTERVAL.of(10, 20);
        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}