package project.dashboard.internal;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class InstantConverter {

    private static final String defaultDatePattern = "yyyy-MM-dd";

    public static Instant toInstant(String date) {
        return toInstant(date, defaultDatePattern);
    }

    public static Instant toInstant(String date, String pattern) {
        var formatter = DateTimeFormatter.ofPattern(pattern);
        var localDate = LocalDate.parse(date, formatter);
        var localDateTime = localDate.atStartOfDay();
        return localDateTime.toInstant(ZoneOffset.UTC);
    }
}
