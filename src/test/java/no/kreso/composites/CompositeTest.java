package no.kreso.composites;

import no.kreso.operations.Operations;
import no.kreso.operations.OperationsDefault;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CompositeTest {

    private static class Interval extends Composite<LocalDate, Interval> {

        private static final Operations<LocalDate> operations = OperationsDefault.unbound();

        private Interval(LocalDate start, LocalDate end) {
            super(start, end, operations);
        }

        public static Interval of(LocalDate start, LocalDate end) {
            return new Interval(start, end);
        }

        @Override
        Interval newInstance(LocalDate start, LocalDate end) {
            return new Interval(start, end);
        }
    }

    LocalDate feb05th = LocalDate.of(2025, 2, 5);
    LocalDate feb10th = LocalDate.of(2025, 2, 10);
    LocalDate feb11th = LocalDate.of(2025, 2, 11);
    LocalDate feb20th = LocalDate.of(2025, 2, 20);

    @Test
    public void creation() {
        Interval interval = Interval.of(feb05th, feb10th);
        assertFalse(interval.isEmpty());
        assertEquals(feb05th, interval.start());
        assertEquals(feb10th, interval.end());
    }

    @Test
    public void creationWithNull() {
        Interval interval;

        interval = Interval.of(null, feb10th);
        assertFalse(interval.isEmpty());
        assertNull(interval.start());
        assertEquals(feb10th, interval.end());

        interval = Interval.of(feb10th, null);
        assertFalse(interval.isEmpty());
        assertEquals(feb10th, interval.start());
        assertNull(interval.end());

        interval = Interval.of(null, null);
        assertFalse(interval.isEmpty());
        assertNull(interval.start());
        assertNull(interval.end());
    }

    @Test
    public void creationWithInvalidInput() {
        Interval interval = Interval.of(feb10th, feb05th);
        assertTrue(interval.isEmpty());
    }

    @Test
    public void subsetOf() {
        Interval interval;

        Interval emptySet = Interval.of(feb05th, feb05th);
        assertTrue(emptySet.subsetOf(emptySet));

        interval = Interval.of(feb05th, feb10th);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = Interval.of(feb05th, null);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = Interval.of(null, feb05th);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = Interval.of(null, null);
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertFalse(interval.subsetOf(emptySet));

        interval = Interval.of(feb20th, feb20th);
        assertTrue(interval.isEmpty());
        assertTrue(interval.subsetOf(interval));
        assertTrue(emptySet.subsetOf(interval));
        assertTrue(interval.subsetOf(emptySet));
    }

    @Test
    public void union() {
        Interval interval;
        Interval other;
        Interval union;

        interval = Interval.of(feb05th, feb10th);
        other = Interval.of(feb10th, feb20th);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertEquals(feb05th, union.start());
        assertEquals(feb20th, union.end());

        interval = Interval.of(null, feb10th);
        other = Interval.of(feb10th, feb20th);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertNull(union.start());
        assertEquals(feb20th, union.end());

        interval = Interval.of(feb05th, feb10th);
        other = Interval.of(null, feb20th);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertNull(union.start());
        assertEquals(feb20th, union.end());

        interval = Interval.of(feb05th, null);
        other = Interval.of(feb10th, feb20th);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertEquals(feb05th, union.start());
        assertNull(union.end());

        interval = Interval.of(feb05th, feb10th);
        other = Interval.of(feb10th, null);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertEquals(feb05th, union.start());
        assertNull(union.end());

        interval = Interval.of(null, feb10th);
        other = Interval.of(feb10th, null);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertNull(union.start());
        assertNull(union.end());

        interval = Interval.of(null, feb10th);
        other = Interval.of(null, null);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertNull(union.start());
        assertNull(union.end());

        interval = Interval.of(null, null);
        other = Interval.of(null, null);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertNull(union.start());
        assertNull(union.end());

        interval = Interval.of(feb05th, feb05th);
        other = Interval.of(feb20th, feb20th);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = Interval.of(feb05th, feb05th);
        other = Interval.of(feb10th, feb11th);
        union = interval.union(other);
        assertFalse(union.isEmpty());
        assertEquals(feb10th, union.start());
        assertEquals(feb11th, union.end());
    }

    @Test
    public void unionDisjoint() {
        Interval interval;
        Interval other;
        Interval union;

        interval = Interval.of(feb05th, feb10th);
        other = Interval.of(feb11th, feb20th);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = Interval.of(null, feb10th);
        other = Interval.of(feb11th, feb20th);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = Interval.of(feb05th, feb10th);
        other = Interval.of(feb11th, null);
        union = interval.union(other);
        assertTrue(union.isEmpty());

        interval = Interval.of(null, feb10th);
        other = Interval.of(feb11th, null);
        union = interval.union(other);
        assertTrue(union.isEmpty());
    }

    @Test
    public void intersection() {
        Interval interval;
        Interval other;
        Interval intersection;

        interval = Interval.of(feb05th, feb11th);
        other = Interval.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb10th, intersection.start());
        assertEquals(feb11th, intersection.end());

        interval = Interval.of(null, feb11th);
        other = Interval.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb10th, intersection.start());
        assertEquals(feb11th, intersection.end());

        interval = Interval.of(feb05th, feb11th);
        other = Interval.of(feb10th, null);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb10th, intersection.start());
        assertEquals(feb11th, intersection.end());

        interval = Interval.of(null, feb11th);
        other = Interval.of(feb10th, null);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb10th, intersection.start());
        assertEquals(feb11th, intersection.end());

        interval = Interval.of(feb05th, null);
        other = Interval.of(feb10th, feb20th);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb10th, intersection.start());
        assertEquals(feb20th, intersection.end());

        interval = Interval.of(feb05th, feb11th);
        other = Interval.of(null, feb20th);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb05th, intersection.start());
        assertEquals(feb11th, intersection.end());

        interval = Interval.of(feb05th, null);
        other = Interval.of(null, feb20th);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb05th, intersection.start());
        assertEquals(feb20th, intersection.end());

        interval = Interval.of(null, null);
        other = Interval.of(null, feb20th);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertNull(intersection.start());
        assertEquals(feb20th, intersection.end());

        interval = Interval.of(feb05th, null);
        other = Interval.of(null, null);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertEquals(feb05th, intersection.start());
        assertNull(intersection.end());

        interval = Interval.of(null, null);
        other = Interval.of(null, null);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertFalse(intersection.isEmpty());
        assertNull(intersection.start());
        assertNull(intersection.end());

        interval = Interval.of(null, feb10th);
        other = Interval.of(feb10th, null);
        intersection = interval.intersection(other);
        assertFalse(interval.isEmpty());
        assertFalse(other.isEmpty());
        assertTrue(intersection.isEmpty());
    }

}
