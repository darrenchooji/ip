package fiona.command;

/**
 * The {@code Command} class represents a user command in the Fiona chatbot.
 * It encapsulates an {@code Action} and any associated arguments.
 */
public class Command {
    /** The action associated with the command. */
    private Action action;

    /** The arguments provided with the command. */
    private String args;

    /**
     * Constructs a {@code Command} object with the specified action and arguments.
     *
     * @param action The action to be performed.
     * @param args The arguments associated with the action, if any.
     */
    public Command(Action action, String args) {
        this.action = action;
        this.args = args;
    }

    /**
     * Returns the action associated with this command.
     *
     * @return The {@code Action} of the command.
     */
    public Action getAction() {
        return action;
    }

    /**
     * Returns the arguments associated with this command.
     *
     * @return The arguments as a string.
     */
    public String getArgs() {
        return args;
    }
}
