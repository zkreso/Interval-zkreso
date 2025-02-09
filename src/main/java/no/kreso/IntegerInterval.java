package no.kreso;

import java.util.Comparator;

public class IntegerInterval extends AbstractInterval<Integer> {

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
    Integer successor(Integer current) {
        return ++current;
    }

    @Override
    int compareTo(Integer a, Integer b) {
        return comparator.compare(a, b);
    }

    public static IntegerInterval of(Integer start, Integer end) {
        return new IntegerInterval(start, end);
    }
}
