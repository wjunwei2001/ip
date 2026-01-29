package pallo.command;

import pallo.exception.PalloException;

/**
 * Parses user input strings into Command objects.
 * Handles all command formats including simple commands (bye, list) and
 * commands with arguments (todo, deadline, event, mark, unmark, delete).
 */
public class Parser {
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_BYE = "bye";
    
    private static final String DELIMITER_BY = " /by ";
    private static final String DELIMITER_FROM = " /from ";
    private static final String DELIMITER_TO = " /to ";

    /**
     * Parses a user input string into a Command object.
     *
     * @param input The raw user input string.
     * @return A Command object representing the parsed command.
     * @throws PalloException If the input is invalid or cannot be parsed.
     */
    public static Command parseCommand(String input) throws PalloException {
        String trimmedInput = input.trim();
        String lowerInput = trimmedInput.toLowerCase();

        if (trimmedInput.isEmpty()) {
            throw new PalloException("OH NO!!! I'm sorry, but I don't know what that means :-(");
        }

        if (lowerInput.equals(COMMAND_BYE)) {
            return new Command(CommandType.BYE, null);
        } else if (lowerInput.equals(COMMAND_LIST)) {
            return new Command(CommandType.LIST, null);
        } else if (lowerInput.equals(COMMAND_MARK)) {
            throw new PalloException("OH NO!!! Please provide a task number to mark.");
        } else if (lowerInput.startsWith(COMMAND_MARK + " ")) {
            String argument = trimmedInput.substring(COMMAND_MARK.length()).trim();
            return new Command(CommandType.MARK, argument);
        } else if (lowerInput.equals(COMMAND_UNMARK)) {
            throw new PalloException("OH NO!!! Please provide a task number to unmark.");
        } else if (lowerInput.startsWith(COMMAND_UNMARK + " ")) {
            String argument = trimmedInput.substring(COMMAND_UNMARK.length()).trim();
            return new Command(CommandType.UNMARK, argument);
        } else if (lowerInput.equals(COMMAND_DELETE)) {
            throw new PalloException("OH NO!!! Please provide a task number to delete.");
        } else if (lowerInput.startsWith(COMMAND_DELETE + " ")) {
            String argument = trimmedInput.substring(COMMAND_DELETE.length()).trim();
            return new Command(CommandType.DELETE, argument);
        } else if (lowerInput.equals(COMMAND_TODO)) {
            throw new PalloException("OH NO!!! The description of a todo cannot be empty.");
        } else if (lowerInput.startsWith(COMMAND_TODO + " ")) {
            String description = trimmedInput.substring(COMMAND_TODO.length()).trim();
            return new Command(CommandType.TODO, description);
        } else if (lowerInput.equals(COMMAND_DEADLINE)) {
            throw new PalloException("OH NO!!! Please use the format: deadline <description> /by <date>");
        } else if (lowerInput.startsWith(COMMAND_DEADLINE + " ")) {
            String rest = trimmedInput.substring(COMMAND_DEADLINE.length()).trim();
            return parseDeadline(rest);
        } else if (lowerInput.equals(COMMAND_EVENT)) {
            throw new PalloException("OH NO!!! Please use the format: event <description> /from <start> /to <end>");
        } else if (lowerInput.startsWith(COMMAND_EVENT + " ")) {
            String rest = trimmedInput.substring(COMMAND_EVENT.length()).trim();
            return parseEvent(rest);
        } else {
            throw new PalloException("OH NO!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private static Command parseDeadline(String rest) throws PalloException {
        int byIndex = rest.toLowerCase().indexOf(DELIMITER_BY);
        if (byIndex == -1) {
            throw new PalloException("OH NO!!! Please use the format: deadline <description> /by <date>");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + DELIMITER_BY.length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new PalloException("OH NO!!! Description and date cannot be empty. Use: deadline <description> /by <date>");
        }
        return new Command(CommandType.DEADLINE, new String[]{description, by});
    }

    private static Command parseEvent(String rest) throws PalloException {
        int fromIndex = rest.toLowerCase().indexOf(DELIMITER_FROM);
        int toIndex = rest.toLowerCase().indexOf(DELIMITER_TO);
        if (fromIndex == -1 || toIndex == -1 || toIndex <= fromIndex) {
            throw new PalloException("OH NO!!! Please use the format: event <description> /from <start> /to <end>");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = rest.substring(toIndex + DELIMITER_TO.length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new PalloException("OH NO!!! Description, start time, and end time cannot be empty. Use: event <description> /from <start> /to <end>");
        }
        return new Command(CommandType.EVENT, new String[]{description, from, to});
    }

    /**
     * Parses a task number from a string argument.
     *
     * @param argument The string containing the task number.
     * @return The parsed task number as an integer.
     * @throws PalloException If the argument is empty or not a valid number.
     */
    public static int parseTaskNumber(String argument) throws PalloException {
        if (argument == null || argument.trim().isEmpty()) {
            throw new PalloException("OH NO!!! Please provide a task number.");
        }
        try {
            return Integer.parseInt(argument.trim());
        } catch (NumberFormatException e) {
            throw new PalloException("OH NO!!! Please provide a valid task number!");
        }
    }
}
