package no.kreso.abstractinterval;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ReverseIntegerIntervalTest {

    @Test
    void creation() {
        ReverseIntegerInterval range = ReverseIntegerInterval.of(10, 5);
        assertEquals(10, range.start());
        assertEquals(5, range.end());
    }

    @Test
    void creationWithNull() {
        ReverseIntegerInterval range;

        range = ReverseIntegerInterval.of(null, 10);
        assertEquals(Integer.MAX_VALUE, range.start());
        assertEquals(10, range.end());

        range = ReverseIntegerInterval.of(5, null);
        assertEquals(5, range.start());
        assertEquals(Integer.MIN_VALUE, range.end());

        range = ReverseIntegerInterval.of(null, null);
        assertEquals(Integer.MAX_VALUE, range.start());
        assertEquals(Integer.MIN_VALUE, range.end());
    }

    @Test
    void creationWithReversedParameters() {
        ReverseIntegerInterval range = ReverseIntegerInterval.of(5, 10);
        assertTrue(range.isEmpty());
    }

    @Test
    void subsetOf() {
        ReverseIntegerInterval emptySet;

        ReverseIntegerInterval range = ReverseIntegerInterval.of(10, 5);
        emptySet = ReverseIntegerInterval.of(20, 20);

        // A set is always its own subset
        assertTrue(range.subsetOf(range));

        // The empty set is a subset of all other sets
        ReverseIntegerInterval otherEmptySet = ReverseIntegerInterval.of(10, 10);
        assertTrue(emptySet.subsetOf(range));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = ReverseIntegerInterval.of(10, 10);
        assertFalse(range.subsetOf(emptySet));
    }

    @Test
    void union() {
        ReverseIntegerInterval range;
        ReverseIntegerInterval other;
        Interval<Integer> union;

        range = ReverseIntegerInterval.of(10, 5);
        other = ReverseIntegerInterval.of(20, 5);
        union = range.union(other);
        assertEquals(20, union.start());
        assertEquals(5, union.end());

        // Unions of disjoint ranges should return the empty range
        // Also make sure that end is exclusive.
        range = ReverseIntegerInterval.of(10, 5);
        other = ReverseIntegerInterval.of(20, 11);
        union = range.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        ReverseIntegerInterval range;
        ReverseIntegerInterval other;
        Interval<Integer> intersection;

        range = ReverseIntegerInterval.of(11, 5);
        other = ReverseIntegerInterval.of(20, 10);
        intersection = range.intersection(other);
        assertEquals(11, intersection.start());
        assertEquals(10, intersection.end());

        // Intersections with the empty set should return the empty set
        range = ReverseIntegerInterval.of(5, 10);
        other = ReverseIntegerInterval.of(5, 5);
        intersection = range.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}
