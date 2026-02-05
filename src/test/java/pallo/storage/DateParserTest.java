package pallo.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class DateParserTest {

    @Test
    public void parseDateTime_isoDateFormat_returnsMidnight() {
        LocalDateTime result = DateParser.parseDateTime("2024-12-02");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(2, result.getDayOfMonth());
        assertEquals(LocalTime.MIDNIGHT, result.toLocalTime());
    }

    @Test
    public void parseDateTime_isoDateWithCompactTime_returnsCorrectDateTime() {
        LocalDateTime result = DateParser.parseDateTime("2024-12-02 1800");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(2, result.getDayOfMonth());
        assertEquals(18, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_isoDateWithColonTime_returnsCorrectDateTime() {
        LocalDateTime result = DateParser.parseDateTime("2024-12-02 18:30");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(2, result.getDayOfMonth());
        assertEquals(18, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_slashFormatWithTime_returnsCorrectDateTime() {
        LocalDateTime result = DateParser.parseDateTime("2/12/2024 1800");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(2, result.getDayOfMonth());
        assertEquals(18, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_slashFormatWithoutTime_returnsMidnight() {
        LocalDateTime result = DateParser.parseDateTime("25/12/2024");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(LocalTime.MIDNIGHT, result.toLocalTime());
    }

    @Test
    public void parseDateTime_nullInput_returnsNull() {
        assertNull(DateParser.parseDateTime(null));
    }

    @Test
    public void parseDateTime_emptyInput_returnsNull() {
        assertNull(DateParser.parseDateTime(""));
        assertNull(DateParser.parseDateTime("   "));
    }

    @Test
    public void parseDateTime_invalidFormat_returnsNull() {
        assertNull(DateParser.parseDateTime("tomorrow"));
        assertNull(DateParser.parseDateTime("next week"));
        assertNull(DateParser.parseDateTime("12-02-2024")); // Wrong order
        assertNull(DateParser.parseDateTime("invalid"));
    }

    @Test
    public void parseDateTime_inputWithWhitespace_trimsAndParses() {
        LocalDateTime result = DateParser.parseDateTime("  2024-12-02  ");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
    }

    @Test
    public void formatDateTime_nullDatetime_returnsOriginalString() {
        String original = "tomorrow";
        String result = DateParser.formatDateTime(null, original);
        assertEquals(original, result);
    }

    @Test
    public void formatDateTime_midnightTime_returnsDateOnly() {
        LocalDateTime midnight = LocalDateTime.of(2024, 12, 25, 0, 0);
        String result = DateParser.formatDateTime(midnight, "2024-12-25");
        assertEquals("Dec 25 2024", result);
    }

    @Test
    public void formatDateTime_withTime_returnsDateAndTime() {
        LocalDateTime withTime = LocalDateTime.of(2024, 12, 25, 14, 30);
        String result = DateParser.formatDateTime(withTime, "2024-12-25 14:30");
        assertEquals("Dec 25 2024 14:30", result);
    }

    @Test
    public void formatDateTimeForFile_validDateTime_returnsIsoFormat() {
        LocalDateTime datetime = LocalDateTime.of(2024, 12, 25, 14, 30);
        String result = DateParser.formatDateTimeForFile(datetime);
        assertEquals("2024-12-25T14:30", result);
    }

    @Test
    public void formatDateTimeForFile_nullDateTime_returnsEmptyString() {
        String result = DateParser.formatDateTimeForFile(null);
        assertEquals("", result);
    }

    @Test
    public void parseDateTimeFromFile_isoFormat_returnsCorrectDateTime() {
        LocalDateTime result = DateParser.parseDateTimeFromFile("2024-12-25T14:30");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTimeFromFile_dateOnlyFormat_returnsMidnight() {
        LocalDateTime result = DateParser.parseDateTimeFromFile("2024-12-25");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(LocalTime.MIDNIGHT, result.toLocalTime());
    }

    @Test
    public void parseDateTimeFromFile_nullInput_returnsNull() {
        assertNull(DateParser.parseDateTimeFromFile(null));
    }

    @Test
    public void parseDateTimeFromFile_emptyInput_returnsNull() {
        assertNull(DateParser.parseDateTimeFromFile(""));
        assertNull(DateParser.parseDateTimeFromFile("   "));
    }

    @Test
    public void parseDateTimeFromFile_invalidFormat_returnsNull() {
        assertNull(DateParser.parseDateTimeFromFile("invalid"));
        assertNull(DateParser.parseDateTimeFromFile("25/12/2024")); // Wrong format for file
    }

    @Test
    public void parseDateTime_invalidDateValues_returnsNull() {
        // Invalid month
        assertNull(DateParser.parseDateTime("2024-13-02"));
        // Invalid day
        assertNull(DateParser.parseDateTime("2024-12-32"));
    }

    @Test
    public void parseDateTime_slashFormatSingleDigits_worksCorrectly() {
        // Single digit day and month
        LocalDateTime result = DateParser.parseDateTime("5/6/2024");
        assertNotNull(result);
        assertEquals(5, result.getDayOfMonth());
        assertEquals(6, result.getMonthValue());
        assertEquals(2024, result.getYear());
    }
}
