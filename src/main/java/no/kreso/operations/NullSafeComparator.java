package no.kreso.operations;


import java.util.Comparator;

public record NullSafeComparator(
        Comparator<Integer> comparator,
        NullSafeComparator.NullInterpretation lower,
        NullSafeComparator.NullInterpretation upper
) {

    public static NullSafeComparator singleMeaning(Comparator<Integer> comparator, NullInterpretation interpretation) {
        return new NullSafeComparator(comparator, interpretation, interpretation);
    }

    public int compareStartToStart(Integer fst, Integer snd) {
        return compare(fst, snd, lower);
    }

    public int compareEndToEnd(Integer fst, Integer snd) {
        return compare(fst, snd, upper);
    }

    public int compareStartToEnd(Integer start, Integer end) {
        if (start == null || end == null) {
            return lower == upper ? 0 : -1;
        }
        return comparator.compare(start, end);
    }

    private int compare(Integer fst, Integer snd, NullInterpretation bound) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return fst == null ? bound.signum() : -bound.signum();
        }
        return comparator.compare(fst, snd);
    }

    enum NullInterpretation {
        POSITIVE_INFINITY(1),
        NEGATIVE_INFINITY(-1);

        private final int signum;

        NullInterpretation(int signum) {
            this.signum = signum;
        }

        public int signum() {
            return signum;
        }
    }
}

