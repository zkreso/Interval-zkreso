package no.kreso.operations;

import no.kreso.interval.Interval;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class UnboundTest {
    
    record Params<T>(Operations<T> unbound, T five, T ten, T eleven, T twenty) { }

    @Test
    public void testDate() {
        Params<LocalDate> params = new Params<>(
                new Unbound<>(Comparator.<LocalDate>naturalOrder()),
                LocalDate.of(2025, 2, 5),
                LocalDate.of(2025, 2, 10),
                LocalDate.of(2025, 2, 11),
                LocalDate.of(2025, 2, 20));
        runTests(params);
    }

    @Test
    public void testInteger() {
        Params<Integer> params = new Params<>(
                new Unbound<>(Comparator.<Integer>naturalOrder()),
                5, 10, 11, 20);
        runTests(params);
    }

    @Test
    public void testIntegerReverse() {
        Params<Integer> params = new Params<>(
                new Unbound<>(Comparator.<Integer>reverseOrder()),
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
        Interval<T> interval = params.unbound.validate(params.five, params.ten);
        assertEquals(interval.start(), params.five);
        assertEquals(interval.end(), params.ten);
    }

    public <T> void creationWithNull(Params<T> params) {
        Interval<T> interval;
        Operations<T> unbound = params.unbound();

        interval = unbound.validate(null, params.ten);
        assertNull(interval.start());
        assertEquals(interval.end(), params.ten);
        assertFalse(unbound.isEmpty(interval));

        interval = unbound.validate(params.ten, null);
        assertEquals(interval.start(), params.ten);
        assertNull(interval.end());
        assertFalse(unbound.isEmpty(interval));

        interval = unbound.validate(null, null);
        assertNull(interval.start());
        assertNull(interval.end());
        assertFalse(unbound.isEmpty(interval));
    }

    public <T> void creationWithReversedParameters(Params<T> params) {
        Interval<T> interval;
        Operations<T> unbound = params.unbound();

        interval = unbound.validate(params.ten, params.five);
        assertTrue(unbound.isEmpty(interval));

        interval = unbound.validate(params.five, params.five);
        assertTrue(unbound.isEmpty(interval));

        interval = unbound.validate(params.ten, null);
        assertFalse(unbound.isEmpty(interval));

        interval = unbound.validate(null, params.ten);
        assertFalse(unbound.isEmpty(interval));
    }

    public <T> void subsetOf(Params<T> params) {
        Operations<T> unbound = params.unbound();
        Interval<T> emptySet = unbound.validate(params.five, params.five);
        Interval<T> interval;

        // 1. A set is always a subset of itself
        // 2. The empty set is a subset of all other sets
        // 3. The empty set has only the empty set as a subset
        interval = unbound.validate(params.five, params.ten);
        assertTrue(unbound.subsetOf(interval, interval));
        assertTrue(unbound.subsetOf(emptySet, interval));
        assertFalse(unbound.subsetOf(interval, emptySet));

        interval = unbound.validate(params.five, null);
        assertTrue(unbound.subsetOf(interval, interval));
        assertTrue(unbound.subsetOf(emptySet, interval));
        assertFalse(unbound.subsetOf(interval, emptySet));

        interval = unbound.validate(null, params.five);
        assertTrue(unbound.subsetOf(interval, interval));
        assertTrue(unbound.subsetOf(emptySet, interval));
        assertFalse(unbound.subsetOf(interval, emptySet));

        interval = unbound.validate(null, null);
        assertTrue(unbound.subsetOf(interval, interval));
        assertTrue(unbound.subsetOf(emptySet, interval));
        assertFalse(unbound.subsetOf(interval, emptySet));

        interval = unbound.validate(params.twenty, params.twenty);
        assertTrue(unbound.subsetOf(interval, interval));
        assertTrue(unbound.subsetOf(emptySet, interval));
        assertTrue(unbound.subsetOf(interval, emptySet));
    }

    public <T> void union(Params<T> params) {
        Operations<T> unbound = params.unbound();
        Interval<T> interval;
        Interval<T> other;
        Interval<T> union;

        interval = unbound.validate(params.five, params.ten);
        other = unbound.validate(params.ten, params.twenty);
        union = unbound.union(interval, other);
        assertEquals(union.start(), params.five);
        assertEquals(union.end(), params.twenty);

        interval = unbound.validate(null, params.ten);
        other = unbound.validate(params.ten, params.twenty);
        union = unbound.union(interval, other);
        assertNull(union.start());
        assertEquals(union.end(), params.twenty);

        interval = unbound.validate(params.five, params.ten);
        other = unbound.validate(null, params.twenty);
        union = unbound.union(interval, other);
        assertNull(union.start());
        assertEquals(union.end(), params.twenty);

        interval = unbound.validate(params.five, null);
        other = unbound.validate(params.ten, params.twenty);
        union = unbound.union(interval, other);
        assertEquals(union.start(), params.five);
        assertNull(union.end());

        interval = unbound.validate(params.five, params.ten);
        other = unbound.validate(params.ten, null);
        union = unbound.union(interval, other);
        assertEquals(union.start(), params.five);
        assertNull(union.end());

        interval = unbound.validate(null, params.ten);
        other = unbound.validate(params.ten, null);
        union = unbound.union(interval, other);
        assertNull(union.end());
        assertNull(union.start());

        interval = unbound.validate(null, params.ten);
        other = unbound.validate(null, null);
        union = unbound.union(interval, other);
        assertNull(union.end());
        assertNull(union.start());

        interval = unbound.validate(null, null);
        other = unbound.validate(null, null);
        union = unbound.union(interval, other);
        assertNull(union.end());
        assertNull(union.start());

        interval = unbound.validate(params.five, params.five);
        other = unbound.validate(params.twenty, params.twenty);
        union = unbound.union(interval, other);
        assertTrue(unbound.isEmpty(union));

        interval = unbound.validate(params.five, params.five);
        other = unbound.validate(params.ten, params.eleven);
        union = unbound.union(interval, other);
        assertEquals(union.start(), params.ten);
        assertEquals(union.end(), params.eleven);
    }

    public <T> void unionDisjoint(Params<T> params) {
        Operations<T> unbound = params.unbound();
        Interval<T> interval;
        Interval<T> other;
        Interval<T> union;

        // Unions of disjoint intervals should return the empty interval
        // Also make sure that it is exclusive
        interval = unbound.validate(params.five, params.ten);
        other = unbound.validate(params.eleven, params.twenty);
        union = unbound.union(interval, other);
        assertTrue(unbound.isEmpty(union));

        interval = unbound.validate(null, params.ten);
        other = unbound.validate(params.eleven, params.twenty);
        union = unbound.union(interval, other);
        assertTrue(unbound.isEmpty(union));

        interval = unbound.validate(params.five, params.ten);
        other = unbound.validate(params.eleven, null);
        union = unbound.union(interval, other);
        assertTrue(unbound.isEmpty(union));

        interval = unbound.validate(null, params.ten);
        other = unbound.validate(params.eleven, null);
        union = unbound.union(interval, other);
        assertTrue(unbound.isEmpty(union));
    }

    public <T> void intersection(Params<T> params) {
        Operations<T> unbound = params.unbound();
        Interval<T> interval;
        Interval<T> other;
        Interval<T> intersection;

        interval = unbound.validate(params.five, params.eleven);
        other = unbound.validate(params.ten, params.twenty);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.eleven);

        interval = unbound.validate(null, params.eleven);
        other = unbound.validate(params.ten, params.twenty);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.eleven);

        interval = unbound.validate(params.five, params.eleven);
        other = unbound.validate(params.ten, null);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.eleven);

        interval = unbound.validate(null, params.eleven);
        other = unbound.validate(params.ten, null);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.eleven);

        interval = unbound.validate(params.five, null);
        other = unbound.validate(params.ten, params.twenty);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.ten);
        assertEquals(intersection.end(), params.twenty);

        interval = unbound.validate(params.five, params.eleven);
        other = unbound.validate(null, params.twenty);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.five);
        assertEquals(intersection.end(), params.eleven);

        interval = unbound.validate(params.five, null);
        other = unbound.validate(null, params.twenty);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.five);
        assertEquals(intersection.end(), params.twenty);

        interval = unbound.validate(null, null);
        other = unbound.validate(null, params.twenty);
        intersection = unbound.intersection(interval, other);
        assertNull(intersection.start());
        assertEquals(intersection.end(), params.twenty);

        interval = unbound.validate(params.five, null);
        other = unbound.validate(null, null);
        intersection = unbound.intersection(interval, other);
        assertEquals(intersection.start(), params.five);
        assertNull(intersection.end());

        interval = unbound.validate(null, null);
        other = unbound.validate(null, null);
        intersection = unbound.intersection(interval, other);
        assertNull(intersection.start());
        assertNull(intersection.end());

        interval = unbound.validate(null, params.ten);
        other = unbound.validate(params.ten, null);
        intersection = unbound.intersection(interval, other);
        assertTrue(unbound.isEmpty(intersection));
    }
}