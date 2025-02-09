package no.kreso.implementations;

import no.kreso.Interval;

import java.time.LocalDate;

/**
 * An implementation of Interval that treats null arguments for lower bound as -infinity and as +infinity for upper
 * bound.
 */
public class NullableInterval implements Interval<LocalDate> {

    private static final NullableInterval EMPTY_INTERVAL = new NullableInterval(LocalDate.EPOCH, LocalDate.EPOCH);

    private final LocalDate start;
    private final LocalDate end;

    private NullableInterval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public static Interval<LocalDate> of(LocalDate start, LocalDate end) {
        if (start != null && end != null && start.isAfter(end)) {
            return EMPTY_INTERVAL;
        }
        return new NullableInterval(start, end);
    }

    @Override
    public LocalDate start() {
        return start;
    }

    @Override
    public LocalDate end() {
        return end;
    }

    @Override
    public boolean subsetOf(Interval<LocalDate> other) {
        // The empty set is a subset of all other sets
        if (this.isEmpty()) {
            return true;
        }
        // The empty set has only itself as a subset
        if (other.isEmpty()) {
            return this.isEmpty();
        }
        return compareStart(this.start, other.start()) >= 0 && compareEnd(this.end, other.end()) <= 0;
    }

    @Override
    public boolean isEmpty() {
        if (start == null || end == null) {
            return false;
        }
        return start.isEqual(end);
    }

    @Override
    public Interval<LocalDate> intersection(Interval<LocalDate> other) {
        if (this.isEmpty()) {
            return this;
        }
        if (other.isEmpty()) {
            return other;
        }
        return of(
                maxStart(other.start()),
                minEnd(other.end())
        );
    }

    private LocalDate minStart(LocalDate other) {
        return compareStart(this.start, other) > 0 ? other : this.start;
    }

    private LocalDate maxStart(LocalDate other) {
        return compareStart(this.start, other) > 0 ? this.start : other;
    }

    private LocalDate minEnd(LocalDate other) {
        return compareEnd(this.end, other) > 0 ? other : this.end;
    }

    private LocalDate maxEnd(LocalDate other) {
        return compareEnd(end, other) > 0 ? end : other;
    }

    private int compareEnd(LocalDate fst, LocalDate snd) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return (fst == null) ? 1 : -1;
        }
        return fst.compareTo(snd);
    }

    private int compareStart(LocalDate fst, LocalDate snd) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return (fst == null) ? -1 : 1;
        }
        return fst.compareTo(snd);
    }

    @Override
    public Interval<LocalDate> union(Interval<LocalDate> other) {
        if (this.isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return this;
        }
        // If this ends before other begins,
        boolean endsBeforeOtherStarts = this.end != null && compareStart(this.end, other.start()) < 0;
        // or if this starts after other ends,
        boolean startsAfterOtherEnds = this.start != null && compareEnd(this.start, other.end()) > 0;
        // then they are disjoint.
        if (endsBeforeOtherStarts || startsAfterOtherEnds) {
            return EMPTY_INTERVAL;
        }
        return of(
                minStart(other.start()),
                maxEnd(other.end())
        );
    }
}
