package no.kreso.implementations;

import no.kreso.Interval;

import java.time.LocalDate;

/**
 * Implementation of Interval for LocalDate that treats null as +infinity for both bounds.
 * Lower bound is treated as inclusive and upper bound as exclusive (when not representing infinity).
 * Unions between disjoint intervals are not supported and return the empty interval.
 */
public class LeftBoundInterval implements Interval<LocalDate> {

    private static final LeftBoundInterval EMPTY_INTERVAL = new LeftBoundInterval(LocalDate.EPOCH, LocalDate.EPOCH);

    private final LocalDate start;
    private final LocalDate end;

    private LeftBoundInterval(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public static Interval<LocalDate> of(LocalDate start, LocalDate end) {
        if (start == null || end != null && start.isAfter(end)) {
            return EMPTY_INTERVAL;
        }
        return new LeftBoundInterval(start, end);
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
        if (this.isEmpty()) {
            return true;
        }
        if (other.isEmpty()) {
            return this.isEmpty();
        }
        return compareTo(this.start, other.start()) >= 0 && compareTo(this.end, other.end()) <= 0;
    }

    private int compareTo(LocalDate fst, LocalDate snd) {
        if (fst == null && snd == null) {
            return 0;
        }
        if (fst == null ^ snd == null) {
            return (fst == null) ? 1 : -1;
        }
        return fst.compareTo(snd);
    }

    @Override
    public boolean isEmpty() {
        return start == null || end != null && start.isEqual(end);
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
                max(start, other.start()),
                min(end, other.end())
        );
    }

    private LocalDate max(LocalDate fst, LocalDate snd) {
        return compareTo(fst, snd) > 0 ? fst : snd;
    }

    private LocalDate min(LocalDate fst, LocalDate snd) {
        return compareTo(fst, snd) < 0 ? fst : snd;
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
        boolean endsBeforeOtherStarts = this.end != null && compareTo(this.end, other.start()) < 0;
        // or if this starts after other ends,
        boolean startsAfterOtherEnds = this.start == null || compareTo(this.start, other.end()) > 0;
        // then they are disjoint.
        if (endsBeforeOtherStarts || startsAfterOtherEnds) {
            return EMPTY_INTERVAL;
        }
        return of(
                min(start, other.start()),
                max(end, other.end())
        );
    }
}
