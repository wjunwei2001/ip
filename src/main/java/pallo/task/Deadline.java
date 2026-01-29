package pallo.task;

import java.time.LocalDateTime;
import pallo.storage.DateParser;

/**
 * Represents a task with a deadline.
 * A Deadline task has a description and a due date/time by which it should be completed.
 */
public class Deadline extends Task {
    protected LocalDateTime by;
    protected String byString; // Keep original string if parsing failed

    /**
     * Constructs a new Deadline task with a string date/time.
     * The date string will be parsed into a LocalDateTime if possible.
     *
     * @param description The description of the deadline task.
     * @param by          The due date/time as a string.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = DateParser.parseDateTime(by);
        this.byString = by; // Keep original string
    }

    /**
     * Constructs a new Deadline task with a LocalDateTime.
     *
     * @param description The description of the deadline task.
     * @param by          The due date/time as a LocalDateTime.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
        this.byString = null;
    }

    @Override
    public String toString() {
        String formattedDate = DateParser.formatDateTime(by, byString);
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }

    @Override
    public String toFileString() {
        int statusValue = (getStatus() == TaskStatus.DONE) ? 1 : 0;
        String dateStr = (by != null) ? DateParser.formatDateTimeForFile(by) : byString;
        return "D | " + statusValue + " | " + getDescription() + " | " + dateStr;
    }

    /**
     * Returns the deadline date/time.
     *
     * @return The due date/time, or null if not parsed successfully.
     */
    public LocalDateTime getBy() {
        return by;
    }
}
