package pallo.task;

import java.util.ArrayList;
import pallo.exception.PalloException;

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

    /**
     * Finds all tasks whose descriptions contain the given keyword.
     * The search is case-insensitive.
     *
     * @param keyword The keyword to search for in task descriptions.
     * @return A list of tasks matching the keyword.
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
