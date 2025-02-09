package no.kreso.implementations;

import no.kreso.Interval;

import java.time.LocalDate;

public class NullableInterval implements Interval<LocalDate> {

    private final LocalDate start;
    private final LocalDate end;

    public NullableInterval(LocalDate start, LocalDate end) {
        if (start != null && end != null && start.isAfter(end)) {
            end = start;
        }
        this.start = start;
        this.end = end;
    }

    public static Interval<LocalDate> of(LocalDate start, LocalDate end) {
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
        return false;
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
        return new NullableInterval(
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
        // If this ends before other begins, then they are disjoint.
        if (this.end != null && compareStart(this.end, other.start()) < 0) {
            return new NullableInterval(this.end, this.end);
        }
        // If this starts after the other ends, then they are disjoint.
        if (this.start != null && compareEnd(this.start, other.end()) > 0) {
            return new NullableInterval(this.start, this.start);
        }
        return new NullableInterval(
                minStart(other.start()),
                maxEnd(other.end())
        );
    }
}
