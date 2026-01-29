package pallo.command;

/**
 * Represents a parsed command from user input.
 * A Command consists of a type (what action to perform) and an optional argument
 * (additional data needed for the action).
 */
public class Command {
    private CommandType type;
    private Object argument;

    /**
     * Constructs a new Command with the specified type and argument.
     *
     * @param type     The type of command to execute.
     * @param argument The argument associated with the command, or null if none.
     */
    public Command(CommandType type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    /**
     * Returns the type of this command.
     *
     * @return The CommandType of this command.
     */
    public CommandType getType() {
        return type;
    }

    /**
     * Returns the raw argument of this command.
     *
     * @return The argument object, or null if none.
     */
    public Object getArgument() {
        return argument;
    }

    /**
     * Returns the argument as a String.
     *
     * @return The argument cast to String.
     */
    public String getStringArgument() {
        return (String) argument;
    }

    /**
     * Returns the argument as a String array.
     *
     * @return The argument cast to String array.
     */
    public String[] getStringArrayArgument() {
        return (String[]) argument;
    }
}
