import java.util.Scanner;

public class Pallo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
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
                if (taskCount == 0) {
                    System.out.println("     No tasks stored yet.");
                } else {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println("     " + (i + 1) + ". " + tasks[i]);
                    }
                }
                System.out.println("    ____________________________________________________________");
                System.out.println();
            } else {
                if (taskCount < 100) {
                    tasks[taskCount] = trimmedInput;
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
