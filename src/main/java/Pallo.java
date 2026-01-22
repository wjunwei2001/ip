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
            } else {
                if (taskCount < 100) {
                    tasks[taskCount] = new Task(trimmedInput);
                    taskCount++;
                    System.out.println("    ____________________________________________________________");
                    System.out.println("     added: " + trimmedInput);
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
