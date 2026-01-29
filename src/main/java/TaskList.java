import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task removeTask(int index) throws PalloException {
        if (index < 0 || index >= tasks.size()) {
            throw new PalloException("OH NO!!! Invalid task number! Please choose a number between 1 and " + tasks.size() + ".");
        }
        return tasks.remove(index);
    }

    public Task getTask(int index) throws PalloException {
        if (index < 0 || index >= tasks.size()) {
            throw new PalloException("OH NO!!! Invalid task number! Please choose a number between 1 and " + tasks.size() + ".");
        }
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks); // Return a copy to prevent external modification
    }
}
