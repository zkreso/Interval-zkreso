package no.kreso.abstractinterval;

import no.kreso.Interval;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class IntegerIntervalTest {

    public static final ComparableInterval<Integer> intervalFactory = ComparableInterval.forType(
            Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE
    );

    @Test
    void creation() {
        Interval<Integer> range = intervalFactory.of(5, 10);
        assertEquals(5, range.start());
        assertEquals(10, range.end());
    }

    @Test
    void creationWithNull() {
        Interval<Integer> range;

        range = intervalFactory.of(null, 10);
        assertEquals(Integer.MIN_VALUE, range.start());
        assertEquals(10, range.end());

        range = intervalFactory.of(5, null);
        assertEquals(5, range.start());
        assertEquals(Integer.MAX_VALUE, range.end());

        range = intervalFactory.of(null, null);
        assertEquals(Integer.MIN_VALUE, range.start());
        assertEquals(Integer.MAX_VALUE, range.end());
    }

    @Test
    void creationWithReversedParameters() {
        Interval<Integer> range = intervalFactory.of(10, 5);
        assertTrue(range.isEmpty());
    }

    @Test
    void subsetOf() {
        Interval<Integer> emptySet;

        Interval<Integer> range = intervalFactory.of(5, 10);
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

        range = intervalFactory.of(5, 10);
        other = intervalFactory.of(5, 20);
        union = range.union(other);
        assertEquals(5, union.start());
        assertEquals(20, union.end());

        // Unions of disjoint ranges should return the empty range
        // Also make sure that end is exclusive.
        range = intervalFactory.of(5, 10);
        other = intervalFactory.of(11, 20);
        union = range.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    void intersection() {
        Interval<Integer> range;
        Interval<Integer> other;
        Interval<Integer> intersection;

        range = intervalFactory.of(5, 11);
        other = intervalFactory.of(10, 20);
        intersection = range.intersection(other);
        assertEquals(10, intersection.start());
        assertEquals(11, intersection.end());

        // Intersections with the empty set should return the empty set
        range = intervalFactory.of(5, 10);
        other = intervalFactory.of(5, 5);
        intersection = range.intersection(other);
        assertTrue(intersection.isEmpty());
    }
}