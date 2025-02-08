package no.kreso;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {

    @Test
    void creation() {
        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        DateRange range = DateRange.of(feb5th, feb10th);
        assertTrue(range.start().isEqual(feb5th));
        assertTrue(range.end().isEqual(feb10th));
    }

    @Test
    void creationWithNull() {
        DateRange range;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);

        range = DateRange.of(null, feb10th);
        assertTrue(range.start().isEqual(LocalDate.MIN));
        assertTrue(range.end().isEqual(feb10th));

        range = DateRange.of(feb5th, null);
        assertTrue(range.start().isEqual(feb5th));
        assertTrue(range.end().isEqual(LocalDate.MAX));

        range = DateRange.of(null, null);
        assertTrue(range.start().isEqual(LocalDate.MIN));
        assertTrue(range.end().isEqual(LocalDate.MAX));
    }

    @Test
    void creationWithReversedParameters() {
        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        DateRange range = DateRange.of(feb10th, feb5th);
        assertTrue(range.start().isEqual(range.end()));
    }

    @Test
    void subsetOf() {
        DateRange range;
        DateRange other;
        DateRange emptySet;

        LocalDate feb5th = LocalDate.of(2025, 2, 5);
        LocalDate feb10th = LocalDate.of(2025, 2, 10);
        LocalDate feb20th = LocalDate.of(2025, 2, 20);

        range = DateRange.of(feb5th, feb10th);
        emptySet = DateRange.of(feb20th, feb20th);

        // A set is always its own subset
        assertTrue(range.subsetOf(range));

        // The empty set is a subset of all other sets (even other empty sets)
        other = DateRange.of(feb10th, feb10th);
        assertTrue(emptySet.subsetOf(range));
        assertTrue(emptySet.subsetOf(other));

        // An empty set has only the empty set as a subset
        emptySet = DateRange.of(feb10th, feb10th);
        assertFalse(range.subsetOf(emptySet));
    }
}