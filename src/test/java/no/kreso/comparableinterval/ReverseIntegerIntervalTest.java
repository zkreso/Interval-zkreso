package no.kreso.comparableinterval;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;


public class ReverseIntegerIntervalTest {

    public static final ComparableInterval<Integer> intervalFactory = ComparableInterval.forType(
            Comparator.reverseOrder(), Integer.MAX_VALUE, Integer.MIN_VALUE
    );

    @Test
    void creation() {
        Interval<Integer> range = intervalFactory.of(10, 5);
        assertEquals(10, range.start());
        assertEquals(5, range.end());
    }

    @Test
    void creationWithNull() {
        Interval<Integer> range;

        range = intervalFactory.of(null, 10);
        assertEquals(Integer.MAX_VALUE, range.start());
        assertEquals(10, range.end());

        range = intervalFactory.of(5, null);
        assertEquals(5, range.start());
        assertEquals(Integer.MIN_VALUE, range.end());

        range = intervalFactory.of(null, null);
        assertEquals(Integer.MAX_VALUE, range.start());
        assertEquals(Integer.MIN_VALUE, range.end());
    }

    @Test
    void creationWithReversedParameters() {
        Interval<Integer> range = intervalFactory.of(5, 10);
        assertTrue(range.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<Integer> emptySet;

        Interval<Integer> range = intervalFactory.of(10, 5);
        emptySet = intervalFactory.of(20, 20);

        // A set is always its own subset
        assertTrue(range.subsetOf(range));

        // The empty set is a subset of all other sets
        Interval<Integer> otherEmptySet = intervalFactory.of(10, 10);
        assertTrue(emptySet.subsetOf(range));
        assertTrue(emptySet.subsetOf(otherEmptySet));

        // An empty set has only the empty set as a subset
        emptySet = intervalFactory.of(10, 10);
        assertFalse(range.subsetOf(emptySet));
    }

    @Test
    void union() {
        Interval<Integer> range;
        Interval<Integer> other;
        Interval<Integer> union;

        range = intervalFactory.of(10, 5);
        other = intervalFactory.of(20, 5);
        union = range.union(other);
        assertEquals(20, union.start());
        assertEquals(5, union.end());

        // Unions of disjoint ranges should return the empty range
        // Also make sure that end is exclusive.
        range = intervalFactory.of(10, 5);
        other = intervalFactory.of(20, 11);
        union = range.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<Integer> range;
        Interval<Integer> other;
        Interval<Integer> intersection;

        range = intervalFactory.of(11, 5);
        other = intervalFactory.of(20, 10);
        intersection = range.intersection(other);
        assertEquals(11, intersection.start());
        assertEquals(10, intersection.end());

        // Intersections with the empty set should return the empty set
        range = intervalFactory.of(5, 10);
        other = intervalFactory.of(5, 5);
        intersection = range.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}
