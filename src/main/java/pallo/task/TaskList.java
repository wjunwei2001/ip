package pallo.task;

import java.util.ArrayList;
import java.util.stream.Collectors;

import pallo.exception.PalloException;

/**
 * Manages a collection of tasks.
 * Provides methods to add, remove, and retrieve tasks from the list.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the given tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        assert task != null : "Task to add should not be null";
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The zero-based index of the task to remove.
     * @return The removed task.
     * @throws PalloException If the index is out of bounds.
     */
    public Task removeTask(int index) throws PalloException {
        if (index < 0 || index >= tasks.size()) {
            throw new PalloException(
                    "OH NO!!! Invalid task number! Please choose a number between 1 and " + tasks.size() + ".");
        }
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the specified index.
     * @throws PalloException If the index is out of bounds.
     */
    public Task getTask(int index) throws PalloException {
        if (index < 0 || index >= tasks.size()) {
            throw new PalloException(
                    "OH NO!!! Invalid task number! Please choose a number between 1 and " + tasks.size() + ".");
        }
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns a copy of all tasks in the list.
     *
     * @return A new ArrayList containing all tasks.
     */
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
        assert keyword != null : "Search keyword should not be null";
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
