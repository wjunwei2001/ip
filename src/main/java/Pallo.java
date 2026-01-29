import java.util.ArrayList;
import java.util.Scanner;

public class Pallo {
    private TaskList tasks;
    private Scanner scanner;
    private Storage storage;

    public Pallo() {
        this.storage = new Storage();
        this.scanner = new Scanner(System.in);
        try {
            ArrayList<Task> loadedTasks = storage.loadTasks();
            this.tasks = new TaskList(loadedTasks);
        } catch (PalloException e) {
            Ui.showError(e.getMessage());
            this.tasks = new TaskList(); // Start with empty list if loading fails
        }
    }

    public void run() {
        Ui.showWelcome();
        
        while (true) {
            try {
                String input = scanner.nextLine();
                Command command = Parser.parseCommand(input);
                
                if (command.getType() == CommandType.BYE) {
                    persistTasks();
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
        boolean shouldSave = false;
        switch (command.getType()) {
        case LIST:
            Ui.showTaskList(tasks);
            break;
        case MARK:
            handleMarkCommand(command);
            shouldSave = true;
            break;
        case UNMARK:
            handleUnmarkCommand(command);
            shouldSave = true;
            break;
        case DELETE:
            handleDeleteCommand(command);
            shouldSave = true;
            break;
        case TODO:
            handleTodoCommand(command);
            shouldSave = true;
            break;
        case DEADLINE:
            handleDeadlineCommand(command);
            shouldSave = true;
            break;
        case EVENT:
            handleEventCommand(command);
            shouldSave = true;
            break;
        default:
            throw new PalloException("OH NO!!! I'm sorry, but I don't know what that means :-(");
        }
        
        if (shouldSave) {
            persistTasks();
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

    private void persistTasks() {
        try {
            storage.saveTasks(tasks.getAllTasks());
        } catch (PalloException e) {
            Ui.showError("Failed to save tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Pallo().run();
    }
}
