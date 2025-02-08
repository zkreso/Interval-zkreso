package no.kreso;

public class IntRange extends AbstractRange<Integer> {

    public IntRange(Integer start, Integer end)
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
        return current++;
    }

    @Override
    Range<Integer> create(Integer start, Integer end) {
        return IntRange.of(start, end);
    }

    public static IntRange of(Integer start, Integer end) {
        return new IntRange(start, end);
    }
}
