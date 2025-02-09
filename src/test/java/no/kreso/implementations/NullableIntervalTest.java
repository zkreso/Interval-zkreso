package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NullableIntervalTest {

    private final LocalDate feb5th = LocalDate.of(2025, 2, 5);
    private final LocalDate feb10th = LocalDate.of(2025, 2, 10);
    private final LocalDate feb11th = LocalDate.of(2025, 2, 11);
    private final LocalDate feb20th = LocalDate.of(2025, 2, 20);

    @Test
    public void creation() {
        Interval<LocalDate> interval = NullableInterval.of(feb5th, feb10th);
        assertTrue(interval.start().isEqual(feb5th));
        assertTrue(interval.end().isEqual(feb10th));
    }

    @Test
    public void creationWithNull() {
        Interval<LocalDate> interval;

        interval = NullableInterval.of(null, feb10th);
        assertNull(interval.start());
        assertTrue(interval.end().isEqual(feb10th));
        assertFalse(interval.isEmpty());

        interval = NullableInterval.of(feb10th, null);
        assertTrue(interval.start().isEqual(feb10th));
        assertNull(interval.end());
        assertFalse(interval.isEmpty());

        interval = NullableInterval.of(null, null);
        assertNull(interval.start());
        assertNull(interval.end());
        assertFalse(interval.isEmpty());
    }

    @Test
    public void creationWithReversedParameters() {
        Interval<LocalDate> interval;

        interval = NullableInterval.of(feb10th, feb5th);
        assertTrue(interval.isEmpty());

        interval = NullableInterval.of(feb10th, null);
        assertFalse(interval.isEmpty());

        interval = NullableInterval.of(null, feb10th);
        assertFalse(interval.isEmpty());
    }

    @Test
    public void subsetOf() {
        Interval<LocalDate> emptySet = NullableInterval.of(feb5th, feb5th);
        Interval<LocalDate> interval;

        // 1. A set is always a subset of itself
        // 2. The empty set is a subset of all other sets
        // 3. The empty set has only the empty set as a subset
        interval = NullableInterval.of(feb5th, feb10th);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = NullableInterval.of(feb5th, null);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = NullableInterval.of(null, feb5th);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = NullableInterval.of(null, null);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = NullableInterval.of(feb20th, feb20th);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertTrue(interval.subsetOf(emptySet));
    }

    @Test
    public void union() {
        Interval<LocalDate> interval;
        Interval<LocalDate> other;
        Interval<LocalDate> union;

        interval = NullableInterval.of(feb5th, feb10th);
        other = NullableInterval.of(feb10th, feb20th);
        union = interval.union(other);
        assertTrue(union.start().isEqual(feb5th));
        assertTrue(union.end().isEqual(feb20th));

        interval = NullableInterval.of(null, feb10th);
        other = NullableInterval.of(feb10th, feb20th);
        union = interval.union(other);
        assertNull(union.start());
        assertTrue(union.end().isEqual(feb20th));

        interval = NullableInterval.of(feb5th, feb10th);
        other = NullableInterval.of(null, feb20th);
        union = interval.union(other);
        assertNull(union.start());
        assertTrue(union.end().isEqual(feb20th));

        interval = NullableInterval.of(feb5th, null);
        other = NullableInterval.of(feb10th, feb20th);
        union = interval.union(other);
        assertTrue(union.start().isEqual(feb5th));
        assertNull(union.end());

        interval = NullableInterval.of(feb5th, feb10th);
        other = NullableInterval.of(feb10th, null);
        union = interval.union(other);
        assertTrue(union.start().isEqual(feb5th));
        assertNull(union.end());

        interval = NullableInterval.of(null, feb10th);
        other = NullableInterval.of(feb10th, null);
        union = interval.union(other);
        assertNull(union.end());
        assertNull(union.start());

        interval = NullableInterval.of(null, feb10th);
        other = NullableInterval.of(null, null);
        union = interval.union(other);
        assertNull(union.end());
        assertNull(union.start());

        interval = NullableInterval.of(null, null);
        other = NullableInterval.of(null, null);
        union = interval.union(other);
        assertNull(union.end());
        assertNull(union.start());
    }

    @Test
    public void unionDisjoint() {
        Interval<LocalDate> interval;
        Interval<LocalDate> other;
        Interval<LocalDate> union;

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that it is exclusive
        interval = NullableInterval.of(feb5th, feb10th);
        other = NullableInterval.of(feb11th, feb20th);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = NullableInterval.of(null, feb10th);
        other = NullableInterval.of(feb11th, feb20th);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = NullableInterval.of(feb5th, feb10th);
        other = NullableInterval.of(feb11th, null);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = NullableInterval.of(null, feb10th);
        other = NullableInterval.of(feb11th, null);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    public void intersection() {
        Interval<LocalDate> interval;
        Interval<LocalDate> other;
        Interval<LocalDate> intersection;

        interval = NullableInterval.of(feb5th, feb11th);
        other = NullableInterval.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb11th));

        interval = NullableInterval.of(null, feb11th);
        other = NullableInterval.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb11th));

        interval = NullableInterval.of(feb5th, feb11th);
        other = NullableInterval.of(feb10th, null);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb11th));

        interval = NullableInterval.of(null, feb11th);
        other = NullableInterval.of(feb10th, null);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb11th));

        interval = NullableInterval.of(feb5th, null);
        other = NullableInterval.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb10th));
        assertTrue(intersection.end().isEqual(feb20th));

        interval = NullableInterval.of(feb5th, feb11th);
        other = NullableInterval.of(null, feb20th);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb5th));
        assertTrue(intersection.end().isEqual(feb11th));

        interval = NullableInterval.of(feb5th, null);
        other = NullableInterval.of(null, feb20th);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb5th));
        assertTrue(intersection.end().isEqual(feb20th));

        interval = NullableInterval.of(null, null);
        other = NullableInterval.of(null, feb20th);
        intersection = interval.intersection(other);
        assertNull(intersection.start());
        assertTrue(intersection.end().isEqual(feb20th));

        interval = NullableInterval.of(feb5th, null);
        other = NullableInterval.of(null, null);
        intersection = interval.intersection(other);
        assertTrue(intersection.start().isEqual(feb5th));
        assertNull(intersection.end());

        interval = NullableInterval.of(null, null);
        other = NullableInterval.of(null, null);
        intersection = interval.intersection(other);
        assertNull(intersection.start());
        assertNull(intersection.end());
    }
}