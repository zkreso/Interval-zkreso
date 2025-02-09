package no.kreso.abstractinterval;

import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;

public class IntegerInterval extends ComparableInterval<Integer> implements Iterable<Integer> {

    private static final Comparator<Integer> comparator = Comparator.naturalOrder();

    public IntegerInterval(Integer start, Integer end) {
        super(start, end, IntegerInterval::new);
    }

    @Override
    Integer minvalue() {
        return Integer.MIN_VALUE;
    }

    @Override
    Integer maxValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    int compareTo(Integer a, Integer b) {
        return comparator.compare(a, b);
    }

    public static IntegerInterval of(Integer start, Integer end) {
        return new IntegerInterval(start, end);
    }

    @Override
    public Iterator<Integer> iterator() {
        return Stream.iterate(start(), (next) -> compareTo(end(), next) > 0, i -> ++i).iterator();
    }
}
