package no.kreso.operations;


import java.util.Comparator;

public record NullSafeComparator<T>(
        Comparator<T> comparator,
        NullSafeComparator.NullInterpretation lower,
        NullSafeComparator.NullInterpretation upper
) {

    public int compareStartToStart(T fst, T snd) {
        return compare(fst, snd, lower);
    }

    public int compareEndToEnd(T fst, T snd) {
        return compare(fst, snd, upper);
    }

    public int compareStartToEnd(T start, T end) {
        if (lower == upper) {
            if (start == null && end == null) {
                return 0;
            }
            if (start == null) {
                return lower().signum();
            }
            if (end == null) {
                return -lower().signum();
            }
        }
        if (start == null || end == null) {
            return -1;
        }
        return comparator.compare(start, end);
    }

    private int compare(T fst, T snd, NullInterpretation bound) {
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

