package no.kreso.composites;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoundIntervalTest {

    @Test
    public void creation() {
        BoundInterval<Integer> interval = BoundInterval.of(5, 10);
        assertEquals(interval.start(), 5);
        assertEquals(interval.end(), 10);
        assertFalse(interval.isEmpty());
    }

    @Test
    public void creationWithNull() {
        BoundInterval<Integer> interval;

        interval = BoundInterval.of(null, 10);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(10, interval.end());
        assertFalse(interval.isEmpty());

        interval = BoundInterval.of(5, null);
        assertEquals(5, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());
        assertFalse(interval.isEmpty());

        interval = BoundInterval.of((Integer) null, null);
        assertEquals(Integer.MIN_VALUE, interval.start());
        assertEquals(Integer.MAX_VALUE, interval.end());
        assertFalse(interval.isEmpty());
    }

    @Test
    public void creationWithReversedParameters() {
        BoundInterval<Integer> interval = BoundInterval.of(10, 5);
        assertTrue(interval.isEmpty());
    }

    @Test
    public void subsetOf() {
        BoundInterval<Integer> interval = BoundInterval.of(5, 10);
        BoundInterval<Integer> emptySet = BoundInterval.of(20, 20);

        assertTrue(emptySet.isEmpty());
        assertFalse(interval.isEmpty());
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(emptySet));
        assertTrue(emptySet.subsetOf(interval));

        BoundInterval<Integer> otherEmptySet = BoundInterval.of(10, 10);
        assertTrue(emptySet.isEmpty());
        assertTrue(emptySet.subsetOf(otherEmptySet));
        assertTrue(otherEmptySet.subsetOf(emptySet));

        assertFalse(interval.subsetOf(emptySet));
        assertFalse(interval.subsetOf(otherEmptySet));
    }

    @Test
    public void union() {
        BoundInterval<Integer> interval = BoundInterval.of(5, 10);
        BoundInterval<Integer> other = BoundInterval.of(5, 20);

        BoundInterval<Integer> union = interval.union(other);
        assertFalse(union.isEmpty());
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        other = BoundInterval.of(11, 20);
        union = interval.union(other);
        assertFalse(other.isEmpty());
        assertFalse(interval.isEmpty());
        assertTrue(union.isEmpty());
    }

    @Test
    public void intersection() {
        BoundInterval<Integer> interval = BoundInterval.of(5, 11);
        assertFalse(interval.isEmpty());

        BoundInterval<Integer> other = BoundInterval.of(10, 20);
        assertFalse(other.isEmpty());

        BoundInterval<Integer> intersection = interval.intersection(other);
        assertFalse(intersection.isEmpty());
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        intersection = other.intersection(interval);
        assertFalse(intersection.isEmpty());
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        interval = BoundInterval.of(5, 10);
        assertFalse(interval.isEmpty());

        other = BoundInterval.of(5, 5);
        assertTrue(other.isEmpty());

        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());

        intersection = other.intersection(interval);
        assertTrue(intersection.isEmpty());

        interval = BoundInterval.of(5, 10);
        assertFalse(interval.isEmpty());
        other = BoundInterval.of(10, 20);
        assertFalse(other.isEmpty());

        intersection = interval.intersection(other);
        assertTrue(intersection.isEmpty());

        intersection = other.intersection(interval);
        assertTrue(intersection.isEmpty());

    }
}