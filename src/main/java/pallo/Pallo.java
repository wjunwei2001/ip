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
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a new Pallo application instance with the default file path.
     */
    public Pallo() {
        this("data/pallo.txt");
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
        assert parts.length == 2 : "Deadline command should have exactly 2 parts";
        Task newTask = new Deadline(parts[0], parts[1]);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleEventCommand(Command command) throws PalloException {
        String[] parts = command.getStringArrayArgument();
        assert parts.length == 3 : "Event command should have exactly 3 parts";
        Task newTask = new Event(parts[0], parts[1], parts[2]);
        tasks.addTask(newTask);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private void handleFindCommand(Command command) {
        String keyword = command.getStringArgument();
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
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
     * Generates a response for the user's chat message.
     *
     * @param input The user's input string.
     * @return The response string from Pallo.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parseCommand(input);
            return executeCommandForGui(command);
        } catch (PalloException e) {
            return e.getMessage();
        }
    }

    private String executeCommandForGui(Command command) throws PalloException {
        StringBuilder response = new StringBuilder();
        boolean shouldSave = false;

        switch (command.getType()) {
        case BYE:
            persistTasks();
            response.append("Bye. I will miss you!");
            break;
        case LIST:
            response.append("Here are the tasks in your list:\n");
            if (tasks.isEmpty()) {
                response.append("No tasks stored yet.");
            } else {
                for (int i = 0; i < tasks.size(); i++) {
                    response.append((i + 1) + "." + tasks.getTask(i));
                    if (i < tasks.size() - 1) {
                        response.append("\n");
                    }
                }
            }
            break;
        case MARK:
            int markNum = Parser.parseTaskNumber(command.getStringArgument());
            Task markTask = tasks.getTask(markNum - 1);
            markTask.markAsDone();
            response.append("Nice! I've marked this task as done:\n  " + markTask);
            shouldSave = true;
            break;
        case UNMARK:
            int unmarkNum = Parser.parseTaskNumber(command.getStringArgument());
            Task unmarkTask = tasks.getTask(unmarkNum - 1);
            unmarkTask.markAsNotDone();
            response.append("OK, I've marked this task as not done yet:\n  " + unmarkTask);
            shouldSave = true;
            break;
        case DELETE:
            int deleteNum = Parser.parseTaskNumber(command.getStringArgument());
            Task removedTask = tasks.removeTask(deleteNum - 1);
            response.append("Noted. I've removed this task:\n  " + removedTask + "\n");
            response.append("Now you have " + tasks.size() + " tasks in the list.");
            shouldSave = true;
            break;
        case TODO:
            String todoDesc = command.getStringArgument();
            if (todoDesc.isEmpty()) {
                throw new PalloException(
                        "OH NO!!! The description of a todo cannot be empty.");
            }
            Task newTodo = new Todo(todoDesc);
            tasks.addTask(newTodo);
            response.append("Got it. I've added this task:\n  " + newTodo + "\n");
            response.append("Now you have " + tasks.size() + " tasks in the list.");
            shouldSave = true;
            break;
        case DEADLINE:
            String[] dlParts = command.getStringArrayArgument();
            Task newDeadline = new Deadline(dlParts[0], dlParts[1]);
            tasks.addTask(newDeadline);
            response.append("Got it. I've added this task:\n  " + newDeadline + "\n");
            response.append("Now you have " + tasks.size() + " tasks in the list.");
            shouldSave = true;
            break;
        case EVENT:
            String[] evParts = command.getStringArrayArgument();
            Task newEvent = new Event(evParts[0], evParts[1], evParts[2]);
            tasks.addTask(newEvent);
            response.append("Got it. I've added this task:\n  " + newEvent + "\n");
            response.append("Now you have " + tasks.size() + " tasks in the list.");
            shouldSave = true;
            break;
        case FIND:
            String keyword = command.getStringArgument();
            ArrayList<Task> matches = tasks.findTasks(keyword);
            response.append("Here are the matching tasks in your list:\n");
            if (matches.isEmpty()) {
                response.append("No matching tasks found.");
            } else {
                for (int i = 0; i < matches.size(); i++) {
                    response.append((i + 1) + "." + matches.get(i));
                    if (i < matches.size() - 1) {
                        response.append("\n");
                    }
                }
            }
            break;
        default:
            throw new PalloException(
                    "OH NO!!! I'm sorry, but I don't know what that means :-(");
        }

        if (shouldSave) {
            persistTasks();
        }

        return response.toString();
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
