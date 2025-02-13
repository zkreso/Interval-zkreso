package no.kreso.composites;

import no.kreso.operations.Bound;
import no.kreso.operations.Operations;

import java.time.LocalDate;
import java.util.Comparator;

public class BoundInterval<T> extends Composite<T, BoundInterval<T>> {

    private static final Bound<Integer> integerOperations = new Bound<>(Comparator.naturalOrder(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    private static final Bound<LocalDate> dateOperations = new Bound<>(Comparator.naturalOrder(), LocalDate.MIN, LocalDate.MAX);

    public BoundInterval(T start, T end, Operations<T> operations) {
        super(start, end, operations);
    }

    public static BoundInterval<Integer> of(Integer start, Integer end) {
        return new BoundInterval<>(start, end, integerOperations);
    }

    public static BoundInterval<LocalDate> of(LocalDate start, LocalDate end) {
        return new BoundInterval<>(start, end, dateOperations);
    }

    @Override
    BoundInterval<T> newInstance(T start, T end) {
        return new BoundInterval<T>(start, end, super.operations());
    }
}
