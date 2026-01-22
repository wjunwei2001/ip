public class Command {
    private CommandType type;
    private Object argument;

    public Command(CommandType type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandType getType() {
        return type;
    }

    public Object getArgument() {
        return argument;
    }

    public String getStringArgument() {
        return (String) argument;
    }

    public String[] getStringArrayArgument() {
        return (String[]) argument;
    }
}
