package pallo.task;

/**
 * Represents a task in the Pallo task manager.
 * A task has a description and a completion status. This is the base class
 * for all task types (Todo, Deadline, Event).
 */
public class Task {
    protected String description;
    protected TaskStatus status;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    @Override
    public String toString() {
        return status.getIcon() + " " + description;
    }

    /**
     * Returns the file storage representation of this task.
     *
     * @return A pipe-delimited string suitable for file storage.
     */
    public String toFileString() {
        int statusValue = (status == TaskStatus.DONE) ? 1 : 0;
        return getTaskType() + " | " + statusValue + " | " + description;
    }

    protected String getTaskType() {
        return "T"; // Default for base Task class, overridden in subclasses
    }
}
