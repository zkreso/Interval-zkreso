package no.kreso.composites;

import no.kreso.operations.Operations;
import no.kreso.operations.Unbound;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Example implementation of Composite for an unbound interval
 */
public class UnboundInterval<T> extends Composite<T, UnboundInterval<T>> {

    private static final Unbound<Integer> integerOperations = new Unbound<Integer>(Comparator.naturalOrder());
    private static final Unbound<LocalDate> dateOperations = new Unbound<LocalDate>(Comparator.naturalOrder());

    public UnboundInterval(T start, T end, Operations<T> operations) {
        super(start, end, operations);
    }

    public static UnboundInterval<Integer> of(Integer start, Integer end) {
        return new UnboundInterval<>(start, end, integerOperations);
    }

    public static UnboundInterval<LocalDate> of(LocalDate start, LocalDate end) {
        return new UnboundInterval<>(start, end, dateOperations);
    }

    @Override
    UnboundInterval<T> newInstance(T start, T end) {
        return new UnboundInterval<>(start, end, super.operations());
    }
}
