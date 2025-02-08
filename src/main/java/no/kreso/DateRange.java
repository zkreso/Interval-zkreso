package no.kreso;

import java.time.LocalDate;

public class DateRange extends AbstractRange<LocalDate> {

    private DateRange(LocalDate start, LocalDate end) {
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

    public static DateRange of(LocalDate start, LocalDate end) {
        return new DateRange(start, end);
    }

    Range<LocalDate> create(LocalDate start, LocalDate end) {
        return of(start, end);
    }
}
