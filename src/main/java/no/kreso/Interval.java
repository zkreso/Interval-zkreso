package no.kreso;


public record Interval<T>(T start, T end) {
    public static <T> Interval<T> of(T start, T end) {
        return new Interval<>(start, end);
    }
}
