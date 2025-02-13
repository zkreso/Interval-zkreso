package no.kreso.interval;

/**
 * Interface whose primary purpose is to be able to return two values at once.
 */
public interface Interval<T> {
    T start();
    T end();
}
