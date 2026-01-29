import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    protected String fromString; // Keep original string if parsing failed
    protected String toString; // Keep original string if parsing failed

    public Event(String description, String from, String to) {
        super(description);
        this.from = DateParser.parseDateTime(from);
        this.to = DateParser.parseDateTime(to);
        this.fromString = from; // Keep original string
        this.toString = to; // Keep original string
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
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

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }
}
