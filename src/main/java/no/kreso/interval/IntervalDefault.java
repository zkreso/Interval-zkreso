package no.kreso.interval;


public record IntervalDefault<T>(T start, T end) implements Interval<T> {
    public static <T> Interval<T> of(T start, T end) {
        return new IntervalDefault<>(start, end);
    }
}
