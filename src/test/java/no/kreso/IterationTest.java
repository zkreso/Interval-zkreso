package no.kreso;

import no.kreso.CustomComparatorTest.ReverseIntegerInterval;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IterationTest {

    @Test
    void testEmptyIntervals() {
        IntegerInterval interval;
        interval = IntegerInterval.of(1, 1);
        doTest(interval, null, null, 0);
        interval = IntegerInterval.of(2, 1);
        doTest(interval, null, null, 0);
        interval = IntegerInterval.of(2, 2);
        doTest(interval, null, null, 0);

        ReverseIntegerInterval reversed;
        reversed = ReverseIntegerInterval.of(1, 1);
        doTest(reversed, null, null, 0);
        reversed = ReverseIntegerInterval.of(1, 2);
        doTest(reversed, null, null, 0);
        reversed = ReverseIntegerInterval.of(2, 2);
        doTest(reversed, null, null, 0);
    }

    @Test
    void testOrdinaryIntervals() {
        IntegerInterval interval;
        interval = IntegerInterval.of(1, 2);
        doTest(interval, 1, 1, 1);
        interval = IntegerInterval.of(1, 3);
        doTest(interval, 1, 2, 2);

        ReverseIntegerInterval reversed;
        reversed = ReverseIntegerInterval.of(2, 1);
        doTest(reversed, 2, 2, 1);
        reversed = ReverseIntegerInterval.of(3, 1);
        doTest(reversed, 3, 2, 2);
    }

    <T extends Comparable<? super T>> void doTest(AbstractInterval<T> interval, T expectedFirst, T expectedLast, int expectedCount) {
        int count = 0;
        T last = null;
        T first = null;
        for (T element : interval) {
            if (first == null) {
                first = element;
            }
            count++;
            last = element;
        }
        assertEquals(expectedCount, count);

        if (expectedFirst == null) {
            assertNull(first);
        } else {
            assertEquals(0, interval.compareTo(expectedFirst, first));
        }
        if (expectedLast == null) {
            assertNull(last);
        } else {
            assertEquals(0, interval.compareTo(expectedLast, last));
        }
    }
}
