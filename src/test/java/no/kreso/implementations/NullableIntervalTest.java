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
}