package pallo.ui;

import java.util.Scanner;
import pallo.task.Task;
import pallo.task.TaskList;
import pallo.exception.PalloException;

public class Ui {
    private static final String HORIZONTAL_LINE = "    ____________________________________________________________";
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }

    public void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Hello! I'm Pallo");
        System.out.println("     What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public void showGoodbye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Bye. I will miss you!");
        System.out.println(HORIZONTAL_LINE);
    }

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

    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalTasks + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public void showTaskRemoved(Task task, int remainingTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + remainingTasks + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

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
}
