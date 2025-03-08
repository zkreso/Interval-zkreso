package no.kreso.interval;

/**
 * Interface whose only purpose is to be able to store two values at once.
 */
public interface Interval<T> {
    T start();
    T end();
}
