public class Task {
    protected String description;
    protected TaskStatus status;

    public Task(String description) {
        this.description = description;
        this.status = TaskStatus.NOT_DONE;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return status == TaskStatus.DONE;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void markAsDone() {
        this.status = TaskStatus.DONE;
    }

    public void markAsNotDone() {
        this.status = TaskStatus.NOT_DONE;
    }

    @Override
    public String toString() {
        return status.getIcon() + " " + description;
    }
}
