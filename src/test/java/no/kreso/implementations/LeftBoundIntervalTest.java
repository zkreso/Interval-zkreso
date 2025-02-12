package no.kreso.implementations;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class LeftBoundIntervalTest {

    private final LocalDate feb5th = LocalDate.of(2025, 2, 5);
    private final LocalDate feb10th = LocalDate.of(2025, 2, 10);
    private final LocalDate feb11th = LocalDate.of(2025, 2, 11);
    private final LocalDate feb20th = LocalDate.of(2025, 2, 20);

    IntervalOperations<LocalDate> leftBond = new LeftBound<>(Comparator.<LocalDate>naturalOrder());

    @Test
    public void creation() {
        IntervalInterface<LocalDate> interval = leftBond.validate(feb5th, feb10th);
        assertEquals(interval.start(), feb5th);
        assertEquals(interval.end(), feb10th);
    }

    @Test
    public void creationWithNull() {
        IntervalInterface<LocalDate> interval;

        interval = leftBond.validate(null, feb10th);
        assertTrue(leftBond.isEmpty(interval));

        interval = leftBond.validate(feb10th, null);
        assertEquals(interval.start(), feb10th);
        assertNull(interval.end());
        assertFalse(leftBond.isEmpty(interval));

        interval = leftBond.validate(null, null);
        assertTrue(leftBond.isEmpty(interval));
    }

    @Test
    public void creationWithReversedParameters() {
        IntervalInterface<LocalDate> interval;

        interval = leftBond.validate(feb10th, feb5th);
        assertTrue(leftBond.isEmpty(interval));
    }

    @Test
    public void subsetOf() {
        IntervalInterface<LocalDate> emptySet = leftBond.validate(feb5th, feb5th);
        IntervalInterface<LocalDate> interval;

        // 1. A set is always a subset of itself
        // 2. The empty set is a subset of all other sets
        // 3. The empty set has only the empty set as a subset
        interval = leftBond.validate(feb5th, feb10th);
        assertTrue(leftBond.subsetOf(interval, interval));
        assertTrue(leftBond.subsetOf(emptySet, interval));
        assertFalse(leftBond.subsetOf(interval, emptySet));

        interval = leftBond.validate(feb5th, null);
        assertTrue(leftBond.subsetOf(interval, interval));
        assertTrue(leftBond.subsetOf(emptySet, interval));
        assertFalse(leftBond.subsetOf(interval, emptySet));

        interval = leftBond.validate(null, feb5th);
        assertTrue(leftBond.subsetOf(interval, interval));
        assertTrue(leftBond.subsetOf(emptySet, interval));
        assertTrue(leftBond.subsetOf(interval, emptySet));

        interval = leftBond.validate(null, null);
        assertTrue(leftBond.subsetOf(interval, interval));
        assertTrue(leftBond.subsetOf(emptySet, interval));
        assertTrue(leftBond.subsetOf(interval, emptySet));

        interval = leftBond.validate(feb20th, feb20th);
        assertTrue(leftBond.subsetOf(interval, interval));
        assertTrue(leftBond.subsetOf(emptySet, interval));
        assertTrue(leftBond.subsetOf(interval, emptySet));
    }

    @Test
    public void union() {
        IntervalInterface<LocalDate> interval;
        IntervalInterface<LocalDate> other;
        IntervalInterface<LocalDate> union;

        interval = leftBond.validate(feb5th, feb10th);
        other = leftBond.validate(feb10th, feb20th);
        union = leftBond.union(interval, other);
        assertEquals(union.start(), feb5th);
        assertEquals(union.end(), feb20th);

        interval = leftBond.validate(feb5th, null);
        other = leftBond.validate(feb10th, feb20th);
        union = leftBond.union(interval, other);
        assertEquals(union.start(), feb5th);
        assertNull(union.end());

        interval = leftBond.validate(feb5th, feb10th);
        other = leftBond.validate(feb10th, null);
        union = leftBond.union(interval, other);
        assertEquals(union.start(), feb5th);
        assertNull(union.end());

        interval = leftBond.validate(feb5th, feb5th);
        other = leftBond.validate(feb10th, null);
        union = leftBond.union(interval, other);
        assertEquals(union.start(), feb10th);
        assertNull(union.end());
    }

    @Test
    public void unionDisjoint() {
        IntervalInterface<LocalDate> interval;
        IntervalInterface<LocalDate> other;
        IntervalInterface<LocalDate> union;

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that it is exclusive
        interval = leftBond.validate(feb5th, feb10th);
        other = leftBond.validate(feb11th, feb20th);
        union = leftBond.union(interval, other);
        assertTrue(leftBond.isEmpty(union));

        interval = leftBond.validate(feb5th, feb10th);
        other = leftBond.validate(feb11th, null);
        union = leftBond.union(interval, other);
        assertTrue(leftBond.isEmpty(union));
    }

    @Test
    public void intersection() {
        IntervalInterface<LocalDate> interval;
        IntervalInterface<LocalDate> other;
        IntervalInterface<LocalDate> intersection;

        interval = leftBond.validate(feb5th, feb11th);
        other = leftBond.validate(feb10th, feb20th);
        intersection = leftBond.intersection(interval, other);
        assertEquals(intersection.start(), feb10th);
        assertEquals(intersection.end(), feb11th);

        interval = leftBond.validate(null, feb11th);
        other = leftBond.validate(feb10th, feb20th);
        intersection = leftBond.intersection(interval, other);
        assertTrue(leftBond.isEmpty(intersection));

        interval = leftBond.validate(feb5th, feb11th);
        other = leftBond.validate(feb10th, null);
        intersection = leftBond.intersection(interval, other);
        assertEquals(intersection.start(), feb10th);
        assertEquals(intersection.end(), feb11th);

        interval = leftBond.validate(feb5th, null);
        other = leftBond.validate(feb10th, feb20th);
        intersection = leftBond.intersection(interval, other);
        assertEquals(intersection.start(), feb10th);
        assertEquals(intersection.end(), feb20th);
    }
}
