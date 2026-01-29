package pallo.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter DISPLAY_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * Parses a date/time string into LocalDateTime.
     * Supports formats:
     * - yyyy-MM-dd (e.g., 2019-12-02)
     * - yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)
     * - yyyy-MM-dd HH:mm (e.g., 2019-12-02 18:00)
     * - d/M/yyyy HHmm (e.g., 2/12/2019 1800)
     * - d/M/yyyy (e.g., 2/12/2019)
     * 
     * If parsing fails, returns null to indicate the string should be kept as-is.
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        String trimmed = dateTimeStr.trim();
        
        try {
            // Try parsing as date only (yyyy-MM-dd)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
                LocalDate date = LocalDate.parse(trimmed, DATE_FORMATTER);
                return date.atStartOfDay();
            }
            
            // Try parsing as date with time (yyyy-MM-dd HHmm)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                String[] parts = trimmed.split(" ");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                String timeStr = parts[1];
                int hour = Integer.parseInt(timeStr.substring(0, 2));
                int minute = Integer.parseInt(timeStr.substring(2, 4));
                LocalTime time = LocalTime.of(hour, minute);
                return LocalDateTime.of(date, time);
            }
            
            // Try parsing as date with time (yyyy-MM-dd HH:mm)
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
                String[] parts = trimmed.split(" ");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1]);
                return LocalDateTime.of(date, time);
            }
            
            // Try parsing as d/M/yyyy HHmm (e.g., 2/12/2019 1800)
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) {
                String[] parts = trimmed.split(" ");
                String[] dateParts = parts[0].split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                LocalDate date = LocalDate.of(year, month, day);
                String timeStr = parts[1];
                int hour = Integer.parseInt(timeStr.substring(0, 2));
                int minute = Integer.parseInt(timeStr.substring(2, 4));
                LocalTime time = LocalTime.of(hour, minute);
                return LocalDateTime.of(date, time);
            }
            
            // Try parsing as d/M/yyyy (e.g., 2/12/2019)
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                String[] dateParts = trimmed.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                LocalDate date = LocalDate.of(year, month, day);
                return date.atStartOfDay();
            }
        } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // If parsing fails, return null to keep as string
            return null;
        }
        
        return null;
    }

    /**
     * Formats a LocalDateTime for display.
     * If datetime is null, returns the original string.
     */
    public static String formatDateTime(LocalDateTime datetime, String originalString) {
        if (datetime == null) {
            return originalString;
        }
        
        // If time is midnight (00:00), show only date
        if (datetime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return datetime.format(DISPLAY_DATE_FORMATTER);
        } else {
            return datetime.format(DISPLAY_DATETIME_FORMATTER);
        }
    }

    /**
     * Formats a LocalDateTime for saving to file (ISO format).
     */
    public static String formatDateTimeForFile(LocalDateTime datetime) {
        if (datetime == null) {
            return "";
        }
        return datetime.toString();
    }

    /**
     * Parses a date/time string from file format.
     */
    public static LocalDateTime parseDateTimeFromFile(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            // Try parsing as LocalDate and convert to LocalDateTime
            try {
                LocalDate date = LocalDate.parse(dateTimeStr);
                return date.atStartOfDay();
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }
}
