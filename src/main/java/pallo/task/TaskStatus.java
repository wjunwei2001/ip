package pallo.task;

/**
 * Represents the completion status of a task.
 * Each status has an associated icon for display purposes.
 */
public enum TaskStatus {
    DONE("[X]"),
    NOT_DONE("[ ]");

    private final String icon;

    TaskStatus(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the display icon for this status.
     *
     * @return The icon string (e.g., "[X]" for done, "[ ]" for not done).
     */
    public String getIcon() {
        return icon;
    }
}
