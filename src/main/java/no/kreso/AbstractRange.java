package no.kreso;

import java.util.Iterator;
import java.util.stream.Stream;

public abstract class AbstractRange<T extends Comparable<? super T>> implements Range<T>, Iterable<T> {

    private final T start;
    private final T end;

    AbstractRange(T start, T end) {
        start = start == null ? minvalue() : start;
        end = end == null ? maxValue() : end;
        if (start.compareTo(end) > 0) {
            end = start;
        }
        this.start = start;
        this.end = end;
    }

    abstract T minvalue();
    abstract T maxValue();
    abstract T successor(T current);
    abstract Range<T> create(T start, T end);

    public T start() {
        return start;
    }

    public T end() {
        return end;
    }

    @Override
    public boolean subsetOf(Range<T> other) {
        if (this.isEmpty()) {
            return true;
        }
        if (other.isEmpty()) {
            return false;
        }
        return start.compareTo(other.start()) >= 0 && end.compareTo(other.end()) <= 0;
    }

    @Override
    public Range<T> and(Range<T> other) {
        return create(
                max(this.start, other.start()),
                min(this.end, other.end())
        );
    }

    @Override
    public Range<T> or(Range<T> other) {
        if (this.end().compareTo(other.start()) < 0 || this.start().compareTo(other.end()) > 0) {
            // Disjoint ranges, return a range with same start and end
            return create(this.start, this.start);
        }
        return create(
                min(this.start, other.start()),
                max(this.end, other.end())
        );
    }

    private T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    private T min(T a, T b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    @Override
    public boolean isEmpty() {
        return start.compareTo(end) == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return Stream.iterate(start, (next) -> end.compareTo(next) > 0, this::successor).iterator();
    }
}
