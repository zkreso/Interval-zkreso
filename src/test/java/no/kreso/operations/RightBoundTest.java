package no.kreso.operations;

import no.kreso.interval.Interval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class RightBoundTest {

    record Params<T>(Operations<T> bound, T five, T ten, T eleven, T twenty) { }

    @Test
    public void testDate() {
        Params<LocalDate> params = new Params<>(
                OperationsDefault.rightBound(),
                LocalDate.of(2025, 2, 5),
                LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 11),
                LocalDate.of(2025, 2, 20));
        runTests(params);
    }

    @Test
    public void testInteger() {
        Params<Integer> params = new Params<>(
                OperationsDefault.rightBound(),
                5, 10, 11, 20);
        runTests(params);
    }

    @Test
    public void testIntegerReverse() {
        NullSafeComparator<Integer> comparator = new NullSafeComparator<>(
                Comparator.<Integer>reverseOrder(),
                NullSafeComparator.NullInterpretation.NEGATIVE_INFINITY,
                NullSafeComparator.NullInterpretation.NEGATIVE_INFINITY
        );
        OperationsDefault<Integer> operations = new OperationsDefault<>(comparator);
        Params<Integer> params = new Params<>(
                operations,
                20, 11, 10, 5);
        runTests(params);
    }

    public <T> void runTests(Params<T> params) {
        creation(params);
        creationWithNull(params);
        creationWithReversedParameters(params);
        subsetOf(params);
        union(params);
        unionDisjoint(params);
        intersection(params);
    }

    public <T> void creation(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> interval = bound.validate(params.five, params.ten);
        assertEquals(interval.start(), params.five);
        assertEquals(interval.end(), params.ten);
    }

    public <T> void creationWithNull(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> interval;

        interval = bound.validate(null, params.ten);
        assertFalse(bound.isEmpty(interval));
        assertNull(interval.start());
        assertEquals(interval.end(), params.ten);

        interval = bound.validate(params.ten, null);
        assertTrue(bound.isEmpty(interval));

        interval = bound.validate(null, null);
        assertTrue(bound.isEmpty(interval));
    }

    public <T> void creationWithReversedParameters(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> interval;

        interval = bound.validate(params.ten, params.five);
        assertTrue(bound.isEmpty(interval));
    }

    public <T> void subsetOf(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> emptySet = bound.validate(params.five, params.five);
        Interval<T> interval;

        // 1. A set is always a subset of itself
        // 2. The empty set is a subset of all other sets
        // 3. The empty set has only the empty set as a subset
        interval = bound.validate(params.five, params.ten);
        assertTrue(bound.subsetOf(interval, interval));
        assertTrue(bound.subsetOf(emptySet, emptySet));
        assertTrue(bound.subsetOf(emptySet, interval));
        assertFalse(bound.subsetOf(interval, emptySet));

        interval = bound.validate(params.five, null);
        assertTrue(bound.subsetOf(interval, interval));
        assertTrue(bound.subsetOf(emptySet, interval));
        assertTrue(bound.subsetOf(interval, emptySet));

        interval = bound.validate(null, params.five);
        assertTrue(bound.subsetOf(interval, interval));
        assertTrue(bound.subsetOf(emptySet, interval));
        assertFalse(bound.subsetOf(interval, emptySet));

        interval = bound.validate(null, null);
        assertTrue(bound.subsetOf(interval, interval));
        assertTrue(bound.subsetOf(emptySet, interval));
        assertTrue(bound.subsetOf(interval, emptySet));

        interval = bound.validate(params.twenty, params.twenty);
        assertTrue(bound.subsetOf(interval, interval));
        assertTrue(bound.subsetOf(emptySet, interval));
        assertTrue(bound.subsetOf(interval, emptySet));
    }

    public <T> void union(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> interval;
        Interval<T> other;
        Interval<T> union;

        interval = bound.validate(params.five, params.ten);
        other = bound.validate(params.ten, params.twenty);
        union = bound.union(interval, other);
        assertEquals(union.start(), params.five);
        assertEquals(union.end(), params.twenty);

        interval = bound.validate(null, params.twenty);
        other = bound.validate(params.five, params.ten);
        union = bound.union(interval, other);
        assertFalse(bound.isEmpty(interval));
        assertNull(union.start());
        assertEquals(union.end(), params.twenty);

        interval = bound.validate(null, params.ten);
        other = bound.validate(params.ten, params.twenty);
        union = bound.union(interval, other);
        assertNull(union.start());
        assertEquals(union.end(), params.twenty);

        interval = bound.validate(params.five, params.five);
        other = bound.validate(null, params.ten);
        union = bound.union(interval, other);
        assertTrue(bound.isEmpty(interval));
        assertFalse(bound.isEmpty(union));
        assertNull(union.start());
        assertEquals(union.end(), params.ten);
    }

    public <T> void unionDisjoint(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> interval;
        Interval<T> other;
        Interval<T> union;

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that it is exclusive
        interval = bound.validate(params.five, params.ten);
        other = bound.validate(params.eleven, params.twenty);
        union = bound.union(interval, other);
        assertTrue(bound.isEmpty(union));

        interval = bound.validate(null, params.ten);
        other = bound.validate(params.eleven, params.twenty);
        union = bound.union(interval, other);
        assertTrue(bound.isEmpty(union));
    }

    public <T> void intersection(Params<T> params) {
        Operations<T> bound = params.bound();
        Interval<T> interval;
        Interval<T> other;
        Interval<T> intersection;

        interval = bound.validate(params.five, params.eleven);
        other = bound.validate(params.ten, params.twenty);
        intersection = bound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.eleven);

        interval = bound.validate(params.eleven, null);
        other = bound.validate(params.five, params.ten);
        intersection = bound.intersection(interval, other);
        assertTrue(bound.isEmpty(intersection));

        interval = bound.validate(params.ten, params.twenty);
        other = bound.validate(null, params.eleven);
        intersection = bound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.eleven);

        interval = bound.validate(null, params.twenty);
        other = bound.validate(params.five, params.ten);
        intersection = bound.intersection(interval, other);
        assertEquals(intersection.start(), params.five);
        assertEquals(intersection.end(), params.ten);
    }
}
