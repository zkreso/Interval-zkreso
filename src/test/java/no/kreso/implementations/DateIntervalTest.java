package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static no.kreso.implementations.IntervalFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class DateIntervalTest {

    @Test
    void creation() {
        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        Interval<LocalDate> interval = DATE_INTERVAL.of(feb5th, feb10th);
        assertTrue(interval.start().isEqual(feb5th));
        assertTrue(interval.end().isEqual(feb10th));
    }

    @Test
    void creationWithNull() {
        Interval<LocalDate> interval;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);

        interval = DATE_INTERVAL.of(null, feb10th);
        assertTrue(interval.start().isEqual(LocalDate.MIN));
        assertTrue(interval.end().isEqual(feb10th));

        interval = DATE_INTERVAL.of(feb5th, null);
        assertTrue(interval.start().isEqual(feb5th));
        assertTrue(interval.end().isEqual(LocalDate.MAX));

        interval = DATE_INTERVAL.of(null, null);
        assertTrue(interval.start().isEqual(LocalDate.MIN));
        assertTrue(interval.end().isEqual(LocalDate.MAX));
    }

    @Test
    void creationWithReversedParameters() {
        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        Interval<LocalDate> interval = DATE_INTERVAL.of(feb10th, feb5th);
        assertTrue(interval.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<LocalDate> emptySet;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        Interval<LocalDate> interval = DATE_INTERVAL.of(feb5th, feb10th);
        emptySet = DATE_INTERVAL.of(feb20th, feb20th);

        // A set is always its own subset
        assertTrue(interval.subsetOf(interval));

        // The empty set is a subset of all other sets
        Interval<LocalDate> otherEmptySet = DATE_INTERVAL.of(feb10th, feb10th);
        assertTrue(emptySet.subsetOf(interval));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = DATE_INTERVAL.of(feb10th, feb10th);
        assertFalse(interval.subsetOf(emptySet));
    }

    @Test
    void union() {
        Interval<LocalDate> interval;
        Interval<LocalDate> other;
        Interval<LocalDate> union;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb11th = LocalDate.of(2025, 2, 11);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        interval = DATE_INTERVAL.of(feb5th, feb10th);
        other = DATE_INTERVAL.of(feb5th, feb20th);
        union = interval.union(other);
        assertTrue(union.start().isEqual(feb5th));
        assertTrue(union.end().isEqual(feb20th));

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = DATE_INTERVAL.of(feb5th, feb10th);
        other = DATE_INTERVAL.of(feb11th, feb20th);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<LocalDate> interval;
        Interval<LocalDate> other;
        Interval<LocalDate> intersection;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb11th = LocalDate.of(2025, 2, 11);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        interval = DATE_INTERVAL.of(feb5th, feb11th);
        other = DATE_INTERVAL.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb11th));

        // Intersections with the empty set should return the empty set
        interval = DATE_INTERVAL.of(feb5th, feb10th);
        other = DATE_INTERVAL.of(feb5th, feb5th);
        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}