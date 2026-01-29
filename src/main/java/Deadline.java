public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toFileString() {
        int statusValue = (getStatus() == TaskStatus.DONE) ? 1 : 0;
        return "D | " + statusValue + " | " + getDescription() + " | " + by;
    }
}
