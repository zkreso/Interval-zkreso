package no.kreso.abstractinterval;

import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;

public class ReverseIntegerInterval extends ComparableInterval<Integer> implements Iterable<Integer> {

    private static final Comparator<Integer> reverseOrder = Comparator.reverseOrder();

    @Override
    Integer minvalue() {
        return Integer.MAX_VALUE;
    }

    @Override
    Integer maxValue() {
        return Integer.MIN_VALUE;
    }

    public ReverseIntegerInterval(Integer high, Integer low) {
        super(high, low, ReverseIntegerInterval::new);
    }

    @Override
    int compareTo(Integer a, Integer b) {
        return reverseOrder.compare(a, b);
    }

    public static ReverseIntegerInterval of(Integer start, Integer end) {
        return new ReverseIntegerInterval(start, end);
    }

    @Override
    public Iterator<Integer> iterator() {
        return Stream.iterate(start(), (next) -> compareTo(end(), next) > 0, i -> --i).iterator();
    }
}
