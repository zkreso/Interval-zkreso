package no.kreso;

import java.time.LocalDate;
import java.util.Comparator;

public class DateInterval extends AbstractInterval<LocalDate> {

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
    LocalDate successor(LocalDate current) {
        return current.plusDays(1);
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
}
