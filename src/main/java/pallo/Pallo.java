package pallo;

import java.util.ArrayList;

import pallo.command.Command;
import pallo.command.CommandType;
import pallo.command.Parser;
import pallo.exception.PalloException;
import pallo.storage.Storage;
import pallo.task.Deadline;
import pallo.task.Event;
import pallo.task.Task;
import pallo.task.TaskList;
import pallo.task.Todo;
import pallo.ui.Ui;

/**
 * Main class for the Pallo task management application.
 * Pallo is a command-line based task manager that allows users to create,
 * manage, and track different types of tasks including todos, deadlines, and events.
 */
public class Pallo {
    private static final String DEFAULT_FILE_PATH = "data/pallo.txt";
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a new Pallo application instance with the default file path.
     */
    public Pallo() {
        this(DEFAULT_FILE_PATH);
    }

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
                }

                String response = executeCommand(command);
                ui.showMessage(response);
            } catch (PalloException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    private String executeCommand(Command command) throws PalloException {
        boolean shouldSave = false;
        String response;

        switch (command.getType()) {
        case LIST:
            response = handleListCommand();
            break;
        case MARK:
            response = handleMarkCommand(command);
            shouldSave = true;
            break;
        case UNMARK:
            response = handleUnmarkCommand(command);
            shouldSave = true;
            break;
        case DELETE:
            response = handleDeleteCommand(command);
            shouldSave = true;
            break;
        case TODO:
            response = handleTodoCommand(command);
            shouldSave = true;
            break;
        case DEADLINE:
            response = handleDeadlineCommand(command);
            shouldSave = true;
            break;
        case EVENT:
            response = handleEventCommand(command);
            shouldSave = true;
            break;
        case FIND:
            response = handleFindCommand(command);
            break;
        default:
            throw new PalloException("OH NO!!! I'm sorry, but I don't know what that means :-(");
        }

        if (shouldSave) {
            persistTasks();
        }

        return response;
    }

    private String handleListCommand() throws PalloException {
        StringBuilder response = new StringBuilder("Here are the tasks in your list:");
        if (tasks.isEmpty()) {
            response.append("\nNo tasks stored yet.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                response.append("\n" + (i + 1) + "." + tasks.getTask(i));
            }
        }
        return response.toString();
    }

    private String handleMarkCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task task = tasks.getTask(taskNumber - 1);
        task.markAsDone();
        return "Nice! I've marked this task as done:\n  " + task;
    }

    private String handleUnmarkCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task task = tasks.getTask(taskNumber - 1);
        task.markAsNotDone();
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    private String handleDeleteCommand(Command command) throws PalloException {
        int taskNumber = Parser.parseTaskNumber(command.getStringArgument());
        Task removedTask = tasks.removeTask(taskNumber - 1);
        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleTodoCommand(Command command) throws PalloException {
        String description = command.getStringArgument();
        Task newTask = new Todo(description);
        tasks.addTask(newTask);
        return "Got it. I've added this task:\n  " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleDeadlineCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        assert parts.length == 2 : "Deadline command should have exactly 2 parts";
        Task newTask = new Deadline(parts[0], parts[1]);
        tasks.addTask(newTask);
        return "Got it. I've added this task:\n  " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleEventCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        assert parts.length == 3 : "Event command should have exactly 3 parts";
        Task newTask = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(newTask);
        return "Got it. I've added this task:\n  " + newTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String handleFindCommand(Command command) {
        String keyword = command.getStringArgument();
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:");
        if (matchingTasks.isEmpty()) {
            response.append("\nNo matching tasks found.");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                response.append("\n" + (i + 1) + "." + matchingTasks.get(i));
            }
        }
        return response.toString();
    }

    private void persistTasks() {
        try {
            storage.save(tasks.getAllTasks());
        } catch (PalloException e) {
            ui.showError("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user's input string.
     * @return The response string from Pallo.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parseCommand(input);
            if (command.getType() == CommandType.BYE) {
                persistTasks();
                return "Bye. I will miss you!";
            }
            return executeCommand(command);
        } catch (PalloException e) {
            return e.getMessage();
        }
    }

    /**
     * Main entry point for the Pallo application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Pallo(DEFAULT_FILE_PATH).run();
    }
}
