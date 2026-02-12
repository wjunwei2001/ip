package pallo.task;

import java.time.LocalDateTime;

import pallo.storage.DateParser;

/**
 * Represents an event task with a start and end time.
 * An Event has a description and a time period during which it occurs.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    protected String fromString; // Keep original string if parsing failed
    protected String toString; // Keep original string if parsing failed

    /**
     * Constructs a new Event task with string date/times.
     * The date strings will be parsed into LocalDateTime if possible.
     *
     * @param description The description of the event.
     * @param from        The start date/time as a string.
     * @param to          The end date/time as a string.
     */
    public Event(String description, String from, String to) {
        super(description);
        assert from != null : "Event start date string should not be null";
        assert to != null : "Event end date string should not be null";
        this.from = DateParser.parseDateTime(from);
        this.to = DateParser.parseDateTime(to);
        this.fromString = from; // Keep original string
        this.toString = to; // Keep original string
    }

    /**
     * Constructs a new Event task with LocalDateTime objects.
     *
     * @param description The description of the event.
     * @param from        The start date/time.
     * @param to          The end date/time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        assert from != null : "Event start LocalDateTime should not be null";
        assert to != null : "Event end LocalDateTime should not be null";
        this.from = from;
        this.to = to;
        this.fromString = null;
        this.toString = null;
    }

    @Override
    public String toString() {
        String formattedFrom = DateParser.formatDateTime(from, fromString);
        String formattedTo = DateParser.formatDateTime(to, toString);
        return "[E]" + super.toString() + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }

    @Override
    public String toFileString() {
        int statusValue = (getStatus() == TaskStatus.DONE) ? 1 : 0;
        String fromStr = (from != null) ? DateParser.formatDateTimeForFile(from) : fromString;
        String toStr = (to != null) ? DateParser.formatDateTimeForFile(to) : toString;
        return "E | " + statusValue + " | " + getDescription() + " | " + fromStr + " | " + toStr;
    }

    /**
     * Returns the event start date/time.
     *
     * @return The start date/time, or null if not parsed successfully.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the event end date/time.
     *
     * @return The end date/time, or null if not parsed successfully.
     */
    public LocalDateTime getTo() {
        return to;
    }
}
