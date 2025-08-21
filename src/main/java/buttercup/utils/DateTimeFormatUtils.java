package buttercup.utils;

import buttercup.exceptions.ButtercupException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeFormatUtils {
    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final String OUTPUT_FORMAT = "MMM dd yyyy HHmm";

    public static LocalDateTime getLocalDateTimeFromString(String dateTimeString) throws ButtercupException {
        try {
            return LocalDateTime.parse(dateTimeString, FORMATTER_1);
        } catch (DateTimeParseException e) {
            // ignore and try next format
        }
        try {
            return LocalDateTime.parse(dateTimeString, FORMATTER_2);
        } catch (DateTimeParseException e) {
            // ignore
            throw new ButtercupException("Invalid date time format. Please use date formats like 'yyyy-MM-dd HHmm' " +
                    "(e.g. 2019-09-15 1800) or 'd/M/yyyy HHmm' (e.g. 13/9/2015 1800)");
        }
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(OUTPUT_FORMAT));
    }
}
