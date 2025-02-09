package no.kreso.abstractinterval;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Stream;

public class DateInterval extends ComparableInterval<LocalDate> implements Iterable<LocalDate> {

    private static final Comparator<LocalDate> comparator = Comparator.naturalOrder();

    private DateInterval(LocalDate start, LocalDate end) {
        super(start, end, DateInterval::new);
    }

    @Override
    LocalDate minvalue() {
        return LocalDate.MIN;
    }

    @Override
    LocalDate maxValue() {
        return LocalDate.MAX;
    }

    @Override
    int compareTo(LocalDate a, LocalDate b) {
        return comparator.compare(a, b);
    }

    public static DateInterval of(LocalDate start, LocalDate end) {
        return new DateInterval(start, end);
    }

    public DateInterval union(DateInterval other) {
        return (DateInterval) super.union(other);
    }

    public DateInterval intersection(DateInterval other) {
        return (DateInterval) super.intersection(other);
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return Stream.iterate(start(), (next) -> compareTo(end(), next) > 0, date -> date.plusDays(1)).iterator();
    }
}
