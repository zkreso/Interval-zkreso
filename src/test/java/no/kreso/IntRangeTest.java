package no.kreso;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IntRangeTest {

    @Test
    void creation() {
        IntRange range = IntRange.of(5, 10);
        assertEquals(5, range.start());
        assertEquals(10, range.end());
    }

    @Test
    void creationWithNull() {
        IntRange range;

        range = IntRange.of(null, 10);
        assertEquals(Integer.MIN_VALUE, range.start());
        assertEquals(10, range.end());

        range = IntRange.of(5, null);
        assertEquals(5, range.start());
        assertEquals(Integer.MAX_VALUE, range.end());

        range = IntRange.of(null, null);
        assertEquals(Integer.MIN_VALUE, range.start());
        assertEquals(Integer.MAX_VALUE, range.end());
    }

    @Test
    void creationWithReversedParameters() {
        IntRange range = IntRange.of(10, 5);
        assertTrue(range.isEmpty());
    }

    @Test
    void subsetOf() {
        IntRange emptySet;

        IntRange range = IntRange.of(5, 10);
        emptySet = IntRange.of(20, 20);

        // A set is always its own subset
        assertTrue(range.subsetOf(range));

        // The empty set is a subset of all other sets
        IntRange otherEmptySet = IntRange.of(10, 10);
        assertTrue(emptySet.subsetOf(range));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = IntRange.of(10, 10);
        assertFalse(range.subsetOf(emptySet));
    }

    @Test
    void union() {
        IntRange range;
        IntRange other;
        IntRange union;

        range = IntRange.of(5, 10);
        other = IntRange.of(5, 20);
        union = (IntRange) range.union(other);
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        // Unions of disjoint ranges should return the empty range
        // Also make sure that end is exclusive.
        range = IntRange.of(5, 10);
        other = IntRange.of(11, 20);
        union = (IntRange) range.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        IntRange range;
        IntRange other;
        IntRange intersection;

        range = IntRange.of(5, 11);
        other = IntRange.of(10, 20);
        intersection = (IntRange) range.intersection(other);
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        // Intersections with the empty set should return the empty set
        range = IntRange.of(5, 10);
        other = IntRange.of(5, 5);
        intersection = (IntRange) range.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}