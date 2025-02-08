package no.kreso;

public class IntegerInterval extends AbstractInterval<Integer> {

    public IntegerInterval(Integer start, Integer end)
    {
        super(start, end);
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
    Interval<Integer> create(Integer start, Integer end) {
        return IntegerInterval.of(start, end);
    }

    public static IntegerInterval of(Integer start, Integer end) {
        return new IntegerInterval(start, end);
    }
}
