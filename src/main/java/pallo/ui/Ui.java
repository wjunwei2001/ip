package pallo.ui;

import java.util.Scanner;
import pallo.task.Task;
import pallo.task.TaskList;
import pallo.exception.PalloException;

/**
 * Handles all user interface interactions for the Pallo application.
 * Responsible for reading user input and displaying messages to the console.
 */
public class Ui {
    private static final String HORIZONTAL_LINE = "    ____________________________________________________________";
    private Scanner scanner;

    /**
     * Constructs a new Ui instance with a Scanner for reading input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The user's input as a String.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Hello! I'm Pallo");
        System.out.println("     What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showGoodbye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Bye. I will miss you!");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     " + message);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public void showLoadingError() {
        showError("Failed to load tasks. Starting with an empty task list.");
    }

    public void showMessage(String message) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     " + message);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Displays all tasks in the given task list.
     *
     * @param tasks The TaskList to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Here are the tasks in your list:");
        if (tasks.isEmpty()) {
            System.out.println("     No tasks stored yet.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    System.out.println("     " + (i + 1) + "." + tasks.getTask(i));
                } catch (PalloException e) {
                    // Should not happen, but handle gracefully
                    showError(e.getMessage());
                }
            }
        }
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task       The task that was added.
     * @param totalTasks The total number of tasks after adding.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalTasks + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Displays a confirmation message when a task is removed.
     *
     * @param task           The task that was removed.
     * @param remainingTasks The number of tasks remaining after removal.
     */
    public void showTaskRemoved(Task task, int remainingTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + remainingTasks + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Displays a confirmation message when a task's status is changed.
     *
     * @param task   The task whose status was changed.
     * @param isDone True if marked as done, false if marked as not done.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        System.out.println(HORIZONTAL_LINE);
        if (isDone) {
            System.out.println("     Nice! I've marked this task as done:");
        } else {
            System.out.println("     OK, I've marked this task as not done yet:");
        }
        System.out.println("       " + task);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    /**
     * Displays the list of tasks that match a search keyword.
     *
     * @param matchingTasks The list of tasks that matched the search.
     */
    public void showFoundTasks(java.util.ArrayList<Task> matchingTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Here are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            System.out.println("     No matching tasks found.");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }
}
