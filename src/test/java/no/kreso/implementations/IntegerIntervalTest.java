package no.kreso.implementations;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class IntegerIntervalTest {
    
    record Params<T>(IntervalOperations<T> bound, T five, T ten, T eleven, T twenty, T minValue, T maxValue) { }

    @Test
    void testInteger() {
        Params<Integer> params = new Params<>(
                new Bound<>(Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE),
                5, 10, 11, 20, Integer.MIN_VALUE, Integer.MAX_VALUE
        );
        runTests(params);
    }

    @Test
    void testIntegerReverse() {
        Params<Integer> params = new Params<>(
                new Bound<>(Comparator.reverseOrder(), Integer.MAX_VALUE, Integer.MIN_VALUE),
                20, 11, 10, 5, Integer.MAX_VALUE, Integer.MIN_VALUE
        );
        runTests(params);
    }

    @Test
    void testDate() {
        Params<LocalDate> params = new Params<>(
                new Bound<>(Comparator.naturalOrder(), LocalDate.MIN, LocalDate.MAX),
                LocalDate.of(2025, 2, 5),
                LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 11),
                LocalDate.of(2025, 2, 20),
                LocalDate.MIN,
                LocalDate.MAX
        );
        runTests(params);
    }

    <T> void runTests(Params<T> params) {
        creation(params);
        creationWithNull(params);
        creationWithReversedParameters(params);
        subsetOf(params);
        union(params);
        intersection(params);
    }

    <T> void creation(Params<T> params) {
        IntervalInterface<T> interval = params.bound().validate(params.five, params.ten);
        assertEquals(params.five, interval.start());
        assertEquals(params.ten, interval.end());
    }

    <T> void creationWithNull(Params<T> params) {
        IntervalInterface<T> interval;

        interval = params.bound().validate(null, params.ten);
        assertEquals(params.minValue(), interval.start());
        assertEquals(params.ten, interval.end());

        interval = params.bound().validate(params.five, null);
        assertEquals(params.five, interval.start());
        assertEquals(params.maxValue(), interval.end());

        interval = params.bound().validate(null, null);
        assertEquals(params.minValue(), interval.start());
        assertEquals(params.maxValue(), interval.end());
    }

    <T> void creationWithReversedParameters(Params<T> params) {
        IntervalInterface<T> interval = params.bound().validate(params.ten, params.five);
        assertTrue(params.bound().isEmpty(interval));
    }

    <T> void subsetOf(Params<T> params) {
        IntervalInterface<T> emptySet;

        IntervalInterface<T> interval = params.bound().validate(params.five, params.ten);
        emptySet = params.bound().validate(params.twenty, params.twenty);

        assertTrue(params.bound().subsetOf(interval, interval));

        IntervalInterface<T> otherEmptySet = params.bound().validate(params.ten, params.ten);
        assertTrue(params.bound().subsetOf(emptySet, interval));
        assertTrue(params.bound().subsetOf(emptySet, otherEmptySet));

        emptySet = params.bound().validate(params.ten, params.ten);
        assertFalse(params.bound().subsetOf(interval, emptySet));
        assertFalse(params.bound().subsetOf(interval, otherEmptySet));
    }

    <T> void union(Params<T> params) {
        IntervalInterface<T> interval;
        IntervalInterface<T> other;
        IntervalInterface<T> union;

        interval = params.bound().validate(params.five, params.ten);
        other = params.bound().validate(params.five, params.twenty);
        union = params.bound().union(interval, other);
        assertEquals(params.five, union.start());
        assertEquals(params.twenty, union.end());

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that end is exclusive.
        interval = params.bound().validate(params.five, params.ten);
        other = params.bound().validate(params.eleven, params.twenty);
        union = params.bound().union(interval, other);
        assertTrue(params.bound().isEmpty(union));
    }

    <T> void intersection(Params<T> params) {
        IntervalInterface<T> interval;
        IntervalInterface<T> other;
        IntervalInterface<T> intersection;

        interval = params.bound().validate(params.five, params.eleven);
        other = params.bound().validate(params.ten, params.twenty);
        intersection = params.bound().intersection(interval, other);
        assertEquals(params.ten, intersection.start());
        assertEquals(params.eleven, intersection.end());

        // Intersections with the empty set should return the empty set
        interval = params.bound().validate(params.five, params.ten);
        other = params.bound().validate(params.five, params.five);
        intersection = params.bound().intersection(interval, other);
        assertTrue(params.bound().isEmpty(intersection));

        // Make sure end is exclusive
        interval = params.bound().validate(params.five, params.ten);
        other = params.bound().validate(params.ten, params.twenty);
        intersection = params.bound().intersection(interval, other);
        assertTrue(params.bound().isEmpty(intersection));
    }
}