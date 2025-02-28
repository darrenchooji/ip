package fiona.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class Ui {
    private static final String LINE = "-------------------------------------------------------------";

    private final BufferedReader reader;
    private final StringBuilder messageLog;

    /**
     * Constructs a {@code Ui} object and initializes the input reader.
     */
    public Ui() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        messageLog = new StringBuilder();
    }

    /**
     * Displays a horizontal line for visual separation.
     */
    public void showLine() {
        // Instead of printing directly, append to the log.
        messageLog.append(LINE).append("\n");
    }

    /**
     * Displays the welcome message when the chatbot starts.
     */
    public void showWelcome() {
        showLine();
        showMessage("Hello! I'm Fiona.");
        showMessage("What can I do for you?");
        showLine();
    }

    /**
     * Displays an error message when there is an issue loading tasks from the file.
     */
    public void showLoadingError() {
        showLine();
        showMessage("Error loading tasks from file. Starting with an empty task list.");
        showLine();
    }

    /**
     * Reads a command from the user.
     *
     * @return The user's input as a string.
     * @throws IOException If an I/O error occurs while reading input.
     */
    public String readCommand() throws IOException {
        return reader.readLine();
    }

    /**
     * Displays a message to the user by appending it to the message log.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        messageLog.append(message).append("\n");
    }

    /**
     * Returns the accumulated messages as a single string and clears the log.
     *
     * @return The output message string.
     */
    public String getMessage() {
        String output = messageLog.toString();
        // Clear the log after retrieving the messages.
        messageLog.setLength(0);
        return output;
    }
}
