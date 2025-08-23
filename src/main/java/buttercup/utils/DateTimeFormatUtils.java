package buttercup.utils;

import buttercup.exceptions.ButtercupException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A util class for processing date time in <code>String</code> formats
 * and converting Strings in accepted date time formats to a
 * <code>LocalDateTime</code> object. This util class also formats a
 * <code>LocaleDateTime</code> into a <code>String</code> in the format
 * <code>MMM dd yyyy HHmm</code> e.g. Aug 06 2025 1400.
 */
public class DateTimeFormatUtils {
    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final String OUTPUT_FORMAT = "MMM dd yyyy HHmm";

    /**
     * Returns a <code>LocalDateTime</code> object if the <code>dateTimeString</code>
     * is of a valid/accepted date time format.
     * @param dateTimeString The date time String to be converted
     * @return A <code>LocalDateTime</code> object based on the date time
     * String provided
     * @throws ButtercupException If the date time String provided is not of an accepted format
     */
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

    /**
     * Parses a <code>LocalDateTime</code> object into a <code>String</code>
     * in the format <code>MMM dd yyyy HHmm</code>.
     * @param dateTime The <code>LocalDateTime</code> object to be parsed.
     * @return A String representation of the date time provided.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(OUTPUT_FORMAT));
    }
}
