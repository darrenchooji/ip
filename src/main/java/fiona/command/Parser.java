package fiona.command;

/**
 * The {@code Parser} class is responsible for interpreting user input and converting it into a {@code Command} object.
 */
public class Parser {

    /**
     * Parses a user command string and converts it into a {@code Command} object.
     *
     * @param fullCommand The full user input string.
     * @return A {@code Command} object representing the parsed action and arguments.
     * @throws FionaException If there is an issue parsing the command.
     */
    public static Command parse(String fullCommand) throws FionaException {
        // Split the input into the command keyword and arguments (if any)
        String[] inputs = fullCommand.trim().split("\\s+", 2);
        Action action = Action.fromString(inputs[0]);

        // Extract arguments if exist
        String args = "";
        if (inputs.length > 1) {
            args = inputs[1].trim();
        }

        if (action == Action.FIND) {
            if (args.matches("\\d{4}-\\d{2}-\\d{2}") || args.matches("\\d{4}-\\d{2}-\\d{2}\\s+\\d{4}")) {
            } else {
                action = Action.FIND_KEYWORD;
            }
        }

        return new Command(action, args);
    }
}
