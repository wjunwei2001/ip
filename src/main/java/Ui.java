public class Ui {
    private static final String HORIZONTAL_LINE = "    ____________________________________________________________";

    public static void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Hello! I'm Pallo");
        System.out.println("     What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public static void showGoodbye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Bye. I will miss you!");
        System.out.println(HORIZONTAL_LINE);
    }

    public static void showError(String message) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     " + message);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public static void showMessage(String message) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     " + message);
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public static void showTaskList(TaskList tasks) {
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

    public static void showTaskAdded(Task task, int totalTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalTasks + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public static void showTaskRemoved(Task task, int remainingTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + remainingTasks + " tasks in the list.");
        System.out.println(HORIZONTAL_LINE);
        System.out.println();
    }

    public static void showTaskMarked(Task task, boolean isDone) {
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
