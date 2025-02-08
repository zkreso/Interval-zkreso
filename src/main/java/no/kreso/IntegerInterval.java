package no.kreso;

public class IntegerInterval extends AbstractInterval<Integer> {

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

    public static IntegerInterval of(Integer start, Integer end) {
        return new IntegerInterval(start, end);
    }
}
