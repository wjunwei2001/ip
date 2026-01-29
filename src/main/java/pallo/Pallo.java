package pallo;

import java.util.ArrayList;
import pallo.storage.Storage;
import pallo.task.TaskList;
import pallo.task.Task;
import pallo.task.Todo;
import pallo.task.Deadline;
import pallo.task.Event;
import pallo.ui.Ui;
import pallo.command.Command;
import pallo.command.CommandType;
import pallo.command.Parser;
import pallo.exception.PalloException;

/**
 * Main class for the Pallo task management application.
 * Pallo is a command-line based task manager that allows users to create,
 * manage, and track different types of tasks including todos, deadlines, and events.
 */
public class Pallo {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a new Pallo application instance.
     * Initializes the UI, storage, and loads existing tasks from the specified file.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Pallo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (PalloException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     * Displays welcome message, processes user commands until exit,
     * and saves tasks upon termination.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                Command command = Parser.parseCommand(input);

                if (command.getType() == CommandType.BYE) {
                    persistTasks();
                    ui.showGoodbye();
                    break;
                } else {
                    executeCommand(command);
                }
            } catch (PalloException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    private void executeCommand(Command command) throws PalloException {
        boolean shouldSave = false;
        switch (command.getType()) {
            case LIST:
                ui.showTaskList(tasks);
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
            case FIND:
                handleFindCommand(command);
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
        ui.showTaskMarked(task, true);
    }

    private void handleUnmarkCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task task = tasks.getTask(taskNumber - 1);
        task.markAsNotDone();
        ui.showTaskMarked(task, false);
    }

    private void handleDeleteCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task removedTask = tasks.removeTask(taskNumber - 1);
        ui.showTaskRemoved(removedTask, tasks.size());
    }

    private void handleTodoCommand(Command command) throws PalloException {
        String description = command.getStringArgument();
        if (description.isEmpty()) {
            throw new PalloException("OH NO!!! The description of a todo cannot be empty.");
        }
        Task newTask = new Todo(description);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleDeadlineCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        Task newTask = new Deadline(parts[0], parts[1]);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleEventCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        Task newTask = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleFindCommand(Command command) {
        String keyword = command.getStringArgument();
        java.util.ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showFoundTasks(matchingTasks);
    }

    private void persistTasks() {
        try {
            storage.save(tasks.getAllTasks());
        } catch (PalloException e) {
            ui.showError("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Main entry point for the Pallo application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Pallo("data/pallo.txt").run();
    }
}
