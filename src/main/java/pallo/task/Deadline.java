package pallo.task;

import java.time.LocalDateTime;
import pallo.storage.DateParser;

public class Deadline extends Task {
    protected LocalDateTime by;
    protected String byString; // Keep original string if parsing failed

    public Deadline(String description, String by) {
        super(description);
        this.by = DateParser.parseDateTime(by);
        this.byString = by; // Keep original string
    }

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

    public LocalDateTime getBy() {
        return by;
    }
}
