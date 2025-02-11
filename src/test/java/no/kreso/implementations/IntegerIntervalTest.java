package no.kreso.implementations;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class IntegerIntervalTest {

    private final Bound<Integer> bound = new Bound<>(Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE);

    @Test
    void creation() {
        IntervalInterface<Integer> interval = bound.validate(5, 10);
        assertEquals(5, interval.start());
        assertEquals(10, interval.end());
    }

    @Test
    void creationWithNull() {
        IntervalInterface<Integer> interval;

        interval = bound.validate(null, 10);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(10, interval.end());

        interval = bound.validate(5, null);
        assertEquals(5, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());

        interval = bound.validate(null, null);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());
    }

    @Test
    void creationWithReversedParameters() {
        IntervalInterface<Integer> interval = bound.validate(10, 5);
        assertTrue(bound.isEmpty(interval));
    }

    @Test
    void subsetOf() {
        IntervalInterface<Integer> emptySet;

        IntervalInterface<Integer> interval = bound.validate(5, 10);
        emptySet = bound.validate(20, 20);

        assertTrue(bound.subsetOf(interval, interval));

        IntervalInterface<Integer> otherEmptySet = bound.validate(10, 10);
        assertTrue(bound.subsetOf(emptySet, interval));
        assertTrue(bound.subsetOf(emptySet, otherEmptySet));

        emptySet = bound.validate(10, 10);
        assertFalse(bound.subsetOf(interval, emptySet));
        assertFalse(bound.subsetOf(interval, otherEmptySet));
    }

    @Test
    void union() {
        IntervalInterface<Integer> interval;
        IntervalInterface<Integer> other;
        IntervalInterface<Integer> union;

        interval = bound.validate(5, 10);
        other = bound.validate(5, 20);
        union = bound.union(interval, other);
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = bound.validate(5, 10);
        other = bound.validate(11, 20);
        union = bound.union(interval, other);
        assertTrue(bound.isEmpty(union));
    }

    @Test
    void intersection() {
        IntervalInterface<Integer> interval;
        IntervalInterface<Integer> other;
        IntervalInterface<Integer> intersection;

        interval = bound.validate(5, 11);
        other = bound.validate(10, 20);
        intersection = bound.intersection(interval, other);
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        // Intersections with the empty set should return the empty set
        interval = bound.validate(5, 10);
        other = bound.validate(5, 5);
        intersection = bound.intersection(interval, other);
        assertTrue(bound.isEmpty(intersection));

        // Make sure end is exclusive
        interval = bound.validate(5, 10);
        other = bound.validate(10, 20);
        intersection = bound.intersection(interval, other);
        assertTrue(bound.isEmpty(intersection));
    }
}