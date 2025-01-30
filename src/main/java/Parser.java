// Parser.java
public class Parser {
    public static Command parse(String fullCommand) throws FionaException {
        String[] inputs = fullCommand.trim().split("\\s+", 2);
        Action action = Action.fromString(inputs[0]);

        String args = "";
        if (inputs.length > 1) {
            args = inputs[1].trim();
        }

        return new Command(action, args);
    }
}
