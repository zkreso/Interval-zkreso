package no.kreso;

import java.time.LocalDate;

public class DateInterval extends AbstractInterval<LocalDate>
{

    private DateInterval(LocalDate start, LocalDate end) {
        super(start, end);
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

    public static DateInterval of(LocalDate start, LocalDate end) {
        return new DateInterval(start, end);
    }

    Interval<LocalDate> create(LocalDate start, LocalDate end) {
        return of(start, end);
    }

    public DateInterval union(DateInterval other) {
        return (DateInterval) super.union(other);
    }

    public DateInterval intersection(DateInterval other) {
        return (DateInterval) super.intersection(other);
    }
}
