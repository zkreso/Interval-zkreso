package no.kreso.abstractinterval;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateIntervalTest {

    @Test
    void creation() {
        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        DateInterval range = DateInterval.of(feb5th, feb10th);
        assertTrue(range.start().isEqual(feb5th));
        assertTrue(range.end().isEqual(feb10th));
    }

    @Test
    void creationWithNull() {
        DateInterval range;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);

        range = DateInterval.of(null, feb10th);
        assertTrue(range.start().isEqual(LocalDate.MIN));
        assertTrue(range.end().isEqual(feb10th));

        range = DateInterval.of(feb5th, null);
        assertTrue(range.start().isEqual(feb5th));
        assertTrue(range.end().isEqual(LocalDate.MAX));

        range = DateInterval.of(null, null);
        assertTrue(range.start().isEqual(LocalDate.MIN));
        assertTrue(range.end().isEqual(LocalDate.MAX));
    }

    @Test
    void creationWithReversedParameters() {
        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        DateInterval range = DateInterval.of(feb10th, feb5th);
        assertTrue(range.isEmpty());
    }

    @Test
    void subsetOf() {
        DateInterval emptySet;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        DateInterval range = DateInterval.of(feb5th, feb10th);
        emptySet = DateInterval.of(feb20th, feb20th);

        // A set is always its own subset
        assertTrue(range.subsetOf(range));

        // The empty set is a subset of all other sets
        DateInterval otherEmptySet = DateInterval.of(feb10th, feb10th);
        assertTrue(emptySet.subsetOf(range));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = DateInterval.of(feb10th, feb10th);
        assertFalse(range.subsetOf(emptySet));
    }

    @Test
    void union() {
        DateInterval range;
        DateInterval other;
        DateInterval union;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb11th = LocalDate.of(2025, 2, 11);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        range = DateInterval.of(feb5th, feb10th);
        other = DateInterval.of(feb5th, feb20th);
        union = range.union(other);
        assertTrue(union.start().isEqual(feb5th));
        assertTrue(union.end().isEqual(feb20th));

        // Unions of disjoint ranges should return the empty range
        // Also make sure that end is exclusive.
        range = DateInterval.of(feb5th, feb10th);
        other = DateInterval.of(feb11th, feb20th);
        union = range.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        DateInterval range;
        DateInterval other;
        DateInterval intersection;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb11th = LocalDate.of(2025, 2, 11);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        range = DateInterval.of(feb5th, feb11th);
        other = DateInterval.of(feb10th, feb20th);
        intersection = range.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb11th));

        // Intersections with the empty set should return the empty set
        range = DateInterval.of(feb5th, feb10th);
        other = DateInterval.of(feb5th, feb5th);
        intersection = range.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}