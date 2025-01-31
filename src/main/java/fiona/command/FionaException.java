package fiona.command;

/**
 * The {@code FionaException} class represents a custom exception used in the Fiona chatbot.
 * It is thrown when an invalid user input or an error occurs within the application.
 */
public class FionaException extends Exception {

    /**
     * Constructs a {@code FionaException} with the specified error message.
     *
     * @param message The detailed error message.
     */
    public FionaException(String message) {
        super(message);
    }
}
