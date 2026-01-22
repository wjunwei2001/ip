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
                try {
                    int taskNumber = Integer.parseInt(trimmedInput.substring(5).trim());
                    if (taskNumber >= 1 && taskNumber <= taskCount) {
                        tasks[taskNumber - 1].markAsDone();
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + tasks[taskNumber - 1]);
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    } else {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Invalid task number!");
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Please provide a valid task number!");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                }
            } else if (trimmedInput.toLowerCase().startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(trimmedInput.substring(7).trim());
                    if (taskNumber >= 1 && taskNumber <= taskCount) {
                        tasks[taskNumber - 1].markAsNotDone();
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + tasks[taskNumber - 1]);
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    } else {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Invalid task number!");
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Please provide a valid task number!");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                }
            } else if (trimmedInput.toLowerCase().startsWith("todo ")) {
                if (taskCount < 100) {
                    String description = trimmedInput.substring(5).trim();
                    if (description.isEmpty()) {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Oops! The description of a todo cannot be empty.");
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    } else {
                        tasks[taskCount] = new Todo(description);
                        taskCount++;
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Got it. I've added this task:");
                        System.out.println("       " + tasks[taskCount - 1]);
                        System.out.println("     Now you have " + taskCount + " tasks in the list.");
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    }
                } else {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Sorry, I can only store up to 100 tasks!");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                }
            } else if (trimmedInput.toLowerCase().startsWith("deadline ")) {
                if (taskCount < 100) {
                    String rest = trimmedInput.substring(9).trim();
                    int byIndex = rest.toLowerCase().indexOf(" /by ");
                    if (byIndex == -1) {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Oops! Please use the format: deadline <description> /by <date>");
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    } else {
                        String description = rest.substring(0, byIndex).trim();
                        String by = rest.substring(byIndex + 5).trim();
                        if (description.isEmpty() || by.isEmpty()) {
                            System.out.println("    ____________________________________________________________");
                            System.out.println("     Oops! Description and date cannot be empty.");
                            System.out.println("    ____________________________________________________________");
                            System.out.println();
                        } else {
                            tasks[taskCount] = new Deadline(description, by);
                            taskCount++;
                            System.out.println("    ____________________________________________________________");
                            System.out.println("     Got it. I've added this task:");
                            System.out.println("       " + tasks[taskCount - 1]);
                            System.out.println("     Now you have " + taskCount + " tasks in the list.");
                            System.out.println("    ____________________________________________________________");
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Sorry, I can only store up to 100 tasks!");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                }
            } else if (trimmedInput.toLowerCase().startsWith("event ")) {
                if (taskCount < 100) {
                    String rest = trimmedInput.substring(6).trim();
                    int fromIndex = rest.toLowerCase().indexOf(" /from ");
                    int toIndex = rest.toLowerCase().indexOf(" /to ");
                    if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
                        System.out.println("    ____________________________________________________________");
                        System.out.println("     Oops! Please use the format: event <description> /from <start> /to <end>");
                        System.out.println("    ____________________________________________________________");
                        System.out.println();
                    } else {
                        String description = rest.substring(0, fromIndex).trim();
                        String from = rest.substring(fromIndex + 7, toIndex).trim();
                        String to = rest.substring(toIndex + 5).trim();
                        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                            System.out.println("    ____________________________________________________________");
                            System.out.println("     Oops! Description, start time, and end time cannot be empty.");
                            System.out.println("    ____________________________________________________________");
                            System.out.println();
                        } else {
                            tasks[taskCount] = new Event(description, from, to);
                            taskCount++;
                            System.out.println("    ____________________________________________________________");
                            System.out.println("     Got it. I've added this task:");
                            System.out.println("       " + tasks[taskCount - 1]);
                            System.out.println("     Now you have " + taskCount + " tasks in the list.");
                            System.out.println("    ____________________________________________________________");
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Sorry, I can only store up to 100 tasks!");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                }
            } else {
                // For backward compatibility, treat plain text as Todo
                if (taskCount < 100) {
                    tasks[taskCount] = new Todo(trimmedInput);
                    taskCount++;
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Got it. I've added this task:");
                    System.out.println("       " + tasks[taskCount - 1]);
                    System.out.println("     Now you have " + taskCount + " tasks in the list.");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                } else {
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     Sorry, I can only store up to 100 tasks!");
                    System.out.println("    ____________________________________________________________");
                    System.out.println();
                }
            }
        }
        
        scanner.close();
    }
}
