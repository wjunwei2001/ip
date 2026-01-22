import java.util.Scanner;

public class Pallo {
    private TaskList tasks;
    private Scanner scanner;

    public Pallo() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        Ui.showWelcome();
        
        while (true) {
            try {
                String input = scanner.nextLine();
                Command command = Parser.parseCommand(input);
                
                if (command.getType() == CommandType.BYE) {
                    Ui.showGoodbye();
                    break;
                } else {
                    executeCommand(command);
                }
            } catch (PalloException e) {
                Ui.showError(e.getMessage());
            }
        }
        
        scanner.close();
    }

    private void executeCommand(Command command) throws PalloException {
        switch (command.getType()) {
        case LIST:
            Ui.showTaskList(tasks);
            break;
        case MARK:
            handleMarkCommand(command);
            break;
        case UNMARK:
            handleUnmarkCommand(command);
            break;
        case DELETE:
            handleDeleteCommand(command);
            break;
        case TODO:
            handleTodoCommand(command);
            break;
        case DEADLINE:
            handleDeadlineCommand(command);
            break;
        case EVENT:
            handleEventCommand(command);
            break;
        default:
            throw new PalloException("OH NO!!! I'm sorry, but I don't know what that means :-(");
        }
    }
    
    private void handleMarkCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task task = tasks.getTask(taskNumber - 1);
        task.markAsDone();
        Ui.showTaskMarked(task, true);
    }

    private void handleUnmarkCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task task = tasks.getTask(taskNumber - 1);
        task.markAsNotDone();
        Ui.showTaskMarked(task, false);
    }

    private void handleDeleteCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task removedTask = tasks.removeTask(taskNumber - 1);
        Ui.showTaskRemoved(removedTask, tasks.size());
    }

    private void handleTodoCommand(Command command) throws PalloException {
        String description = command.getStringArgument();
        if (description.isEmpty()) {
            throw new PalloException("OH NO!!! The description of a todo cannot be empty.");
        }
        Task newTask = new Todo(description);
        tasks.addTask(newTask);
        Ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleDeadlineCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        Task newTask = new Deadline(parts[0], parts[1]);
        tasks.addTask(newTask);
        Ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleEventCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        Task newTask = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(newTask);
        Ui.showTaskAdded(newTask, tasks.size());
    }

    public static void main(String[] args) {
        new Pallo().run();
    }
}
