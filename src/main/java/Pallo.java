import java.util.Scanner;

public class Pallo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("    ____________________________________________________________");
        System.out.println("     Hello! I'm Pallo");
        System.out.println("     What can I do for you?");
        System.out.println("    ____________________________________________________________");
        System.out.println();
        
        String input;
        while (true) {
            input = scanner.nextLine();
            System.out.println("    ____________________________________________________________");
            System.out.println("     " + input);
            System.out.println("    ____________________________________________________________");
            System.out.println();
            
            if (input.trim().equalsIgnoreCase("bye")) {
                break;
            }
        }
        
        System.out.println("    ____________________________________________________________");
        System.out.println("     Bye. I will miss you!");
        System.out.println("    ____________________________________________________________");
        scanner.close();
    }
}
