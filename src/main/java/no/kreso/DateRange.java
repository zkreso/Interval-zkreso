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

    public static Range<LocalDate> of(LocalDate start, LocalDate end) {
        return new DateRange(start, end);
    }

    Range<LocalDate> create(LocalDate start, LocalDate end) {
        return of(start, end);
    }
}
