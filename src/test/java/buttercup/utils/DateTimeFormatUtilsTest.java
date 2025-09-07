package buttercup.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import buttercup.exceptions.ButtercupException;

public class DateTimeFormatUtilsTest {
    @Test
    public void getLocalDateTimeFromString_validDateTimeFormat_success() throws ButtercupException {
        assertEquals(LocalDateTime.of(2025, 1, 1, 18, 0),
                DateTimeFormatUtils.getLocalDateTimeFromString("2025-01-01 1800"));

        assertEquals(LocalDateTime.of(2025, 1, 1, 18, 0),
                DateTimeFormatUtils.getLocalDateTimeFromString("1/1/2025 1800"));
    }

    @Test
    public void getLocalDateTimeFromString_invalidDateTimeFormat_exceptionThrown() {
        try {
            assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0),
                    DateTimeFormatUtils.getLocalDateTimeFromString("1 Jan 2025 0000"));
            fail();
        } catch (Exception e) {
            assertEquals("Invalid date time format. Please use date formats like 'yyyy-MM-dd HHmm' "
                            + "(e.g. 2019-09-15 1800) or 'd/M/yyyy HHmm' (e.g. 13/9/2015 1800)",
                    e.getMessage());
        }
    }

    @Test
    public void getLocalDateTimeFromString_nullInput_exceptionThrown() {
        try {
            assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0),
                    DateTimeFormatUtils.getLocalDateTimeFromString(null));
            fail();
        } catch (Exception e) {
            assertThrows(NullPointerException.class, () ->
                    DateTimeFormatUtils.getLocalDateTimeFromString(null));
        }
    }

    @Test
    public void formatDateTime_validDateTime_success() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 18, 0);
        assertEquals("Jan 01 2025 18:00",
                DateTimeFormatUtils.formatDateTime(dateTime));
    }
}
