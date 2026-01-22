import java.util.Scanner;

public class Pallo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        
        System.out.println("    ____________________________________________________________");
        System.out.println("     Hello! I'm Pallo");
        System.out.println("     What can I do for you?");
        System.out.println("    ____________________________________________________________");
        System.out.println();
        
        String input;
        while (true) {
            try {
                input = scanner.nextLine();
                String trimmedInput = input.trim();
                
                if (trimmedInput.equalsIgnoreCase("bye")) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Bye. I will miss you!");
                    System.out.println("    ____________________________________________________________");
                    break;
                } else if (trimmedInput.equalsIgnoreCase("list")) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Here are the tasks in your list:");
                    if (taskCount == 0) {
                        System.out.println("     No tasks stored yet.");
                    } else {
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println("     " + (i + 1) + "." + tasks[i]);
                        }
                    }
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                } else if (trimmedInput.toLowerCase().startsWith("mark ")) {
                    handleMarkCommand(trimmedInput, tasks, taskCount);
                } else if (trimmedInput.toLowerCase().startsWith("unmark ")) {
                    handleUnmarkCommand(trimmedInput, tasks, taskCount);
                } else if (trimmedInput.equalsIgnoreCase("todo")) {
                    throw new PalloException("OH NO!!! The description of a todo cannot be empty.");
                } else if (trimmedInput.toLowerCase().startsWith("todo ")) {
                    handleTodoCommand(trimmedInput, tasks, taskCount);
                    taskCount++;
                } else if (trimmedInput.equalsIgnoreCase("deadline")) {
                    throw new PalloException("OH NO!!! Please use the format: deadline <description> /by <date>");
                } else if (trimmedInput.toLowerCase().startsWith("deadline ")) {
                    handleDeadlineCommand(trimmedInput, tasks, taskCount);
                    taskCount++;
                } else if (trimmedInput.equalsIgnoreCase("event")) {
                    throw new PalloException("OH NO!!! Please use the format: event <description> /from <start> /to <end>");
                } else if (trimmedInput.toLowerCase().startsWith("event ")) {
                    handleEventCommand(trimmedInput, tasks, taskCount);
                    taskCount++;
                } else {
                    throw new PalloException("OH NO!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (PalloException e) {
                System.out.println("    ____________________________________________________________");
                System.out.println("     " + e.getMessage());
                System.out.println("    ____________________________________________________________");
                System.out.println();
            }
        }
        
        scanner.close();
    }
    
    private static void handleMarkCommand(String input, Task[] tasks, int taskCount) throws PalloException {
        String numberStr = input.substring(5).trim();
        if (numberStr.isEmpty()) {
            throw new PalloException("OH NO!!! Please provide a task number to mark.");
        }
        try {
            int taskNumber = Integer.parseInt(numberStr);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new PalloException("OH NO!!! Invalid task number! Please choose a number between 1 and " + taskCount + ".");
            }
            tasks[taskNumber - 1].markAsDone();
            System.out.println("    ____________________________________________________________");
            System.out.println("     Nice! I've marked this task as done:");
            System.out.println("       " + tasks[taskNumber - 1]);
            System.out.println("    ____________________________________________________________");
            System.out.println();
        } catch (NumberFormatException e) {
            throw new PalloException("OH NO!!! Please provide a valid task number!");
        }
    }
    
    private static void handleUnmarkCommand(String input, Task[] tasks, int taskCount) throws PalloException {
        String numberStr = input.substring(7).trim();
        if (numberStr.isEmpty()) {
            throw new PalloException("OH NO!!! Please provide a task number to unmark.");
        }
        try {
            int taskNumber = Integer.parseInt(numberStr);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new PalloException("OH NO!!! Invalid task number! Please choose a number between 1 and " + taskCount + ".");
            }
            tasks[taskNumber - 1].markAsNotDone();
            System.out.println("    ____________________________________________________________");
            System.out.println("     OK, I've marked this task as not done yet:");
            System.out.println("       " + tasks[taskNumber - 1]);
            System.out.println("    ____________________________________________________________");
            System.out.println();
        } catch (NumberFormatException e) {
            throw new PalloException("OH NO!!! Please provide a valid task number!");
        }
    }
    
    private static void handleTodoCommand(String input, Task[] tasks, int taskCount) throws PalloException {
        if (taskCount >= 100) {
            throw new PalloException("OH NO!!! I can only store up to 100 tasks!");
        }
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new PalloException("OH NO!!! The description of a todo cannot be empty.");
        }
        tasks[taskCount] = new Todo(description);
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks[taskCount]);
        System.out.println("     Now you have " + (taskCount + 1) + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
        System.out.println();
    }
    
    private static void handleDeadlineCommand(String input, Task[] tasks, int taskCount) throws PalloException {
        if (taskCount >= 100) {
            throw new PalloException("OH NO!!! I can only store up to 100 tasks!");
        }
        String rest = input.substring(9).trim();
        int byIndex = rest.toLowerCase().indexOf(" /by ");
        if (byIndex == -1) {
            throw new PalloException("OH NO!!! Please use the format: deadline <description> /by <date>");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 5).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new PalloException("OH NO!!! Description and date cannot be empty. Use: deadline <description> /by <date>");
        }
        tasks[taskCount] = new Deadline(description, by);
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks[taskCount]);
        System.out.println("     Now you have " + (taskCount + 1) + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
        System.out.println();
    }
    
    private static void handleEventCommand(String input, Task[] tasks, int taskCount) throws PalloException {
        if (taskCount >= 100) {
            throw new PalloException("OH NO!!! I can only store up to 100 tasks!");
        }
        String rest = input.substring(6).trim();
        int fromIndex = rest.toLowerCase().indexOf(" /from ");
        int toIndex = rest.toLowerCase().indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
            throw new PalloException("OH NO!!! Please use the format: event <description> /from <start> /to <end>");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + 7, toIndex).trim();
        String to = rest.substring(toIndex + 5).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new PalloException("OH NO!!! Description, start time, and end time cannot be empty. Use: event <description> /from <start> /to <end>");
        }
        tasks[taskCount] = new Event(description, from, to);
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + tasks[taskCount]);
        System.out.println("     Now you have " + (taskCount + 1) + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
        System.out.println();
    }
}
