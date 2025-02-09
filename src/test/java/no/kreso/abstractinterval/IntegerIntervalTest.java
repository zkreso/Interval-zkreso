package no.kreso.abstractinterval;

import no.kreso.abstractinterval.IntegerInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerIntervalTest {

    @Test
    void creation() {
        IntegerInterval range = IntegerInterval.of(5, 10);
        assertEquals(5, range.start());
        assertEquals(10, range.end());
    }

    @Test
    void creationWithNull() {
        IntegerInterval range;

        range = IntegerInterval.of(null, 10);
        assertEquals(Integer.MIN_VALUE, range.start());
        assertEquals(10, range.end());

        range = IntegerInterval.of(5, null);
        assertEquals(5, range.start());
        assertEquals(Integer.MAX_VALUE, range.end());

        range = IntegerInterval.of(null, null);
        assertEquals(Integer.MIN_VALUE, range.start());
        assertEquals(Integer.MAX_VALUE, range.end());
    }

    @Test
    void creationWithReversedParameters() {
        IntegerInterval range = IntegerInterval.of(10, 5);
        assertTrue(range.isEmpty());
    }

    @Test
    void subsetOf() {
        IntegerInterval emptySet;

        IntegerInterval range = IntegerInterval.of(5, 10);
        emptySet = IntegerInterval.of(20, 20);

        // A set is always its own subset
        assertTrue(range.subsetOf(range));

        // The empty set is a subset of all other sets
        IntegerInterval otherEmptySet = IntegerInterval.of(10, 10);
        assertTrue(emptySet.subsetOf(range));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = IntegerInterval.of(10, 10);
        assertFalse(range.subsetOf(emptySet));
    }

    @Test
    void union() {
        IntegerInterval range;
        IntegerInterval other;
        IntegerInterval union;

        range = IntegerInterval.of(5, 10);
        other = IntegerInterval.of(5, 20);
        union = (IntegerInterval) range.union(other);
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        // Unions of disjoint ranges should return the empty range
        // Also make sure that end is exclusive.
        range = IntegerInterval.of(5, 10);
        other = IntegerInterval.of(11, 20);
        union = (IntegerInterval) range.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        IntegerInterval range;
        IntegerInterval other;
        IntegerInterval intersection;

        range = IntegerInterval.of(5, 11);
        other = IntegerInterval.of(10, 20);
        intersection = (IntegerInterval) range.intersection(other);
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        // Intersections with the empty set should return the empty set
        range = IntegerInterval.of(5, 10);
        other = IntegerInterval.of(5, 5);
        intersection = (IntegerInterval) range.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}