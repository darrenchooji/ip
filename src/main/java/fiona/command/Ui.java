package fiona.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The {@code Ui} class handles user interaction in the Fiona chatbot.
 * It is responsible for displaying messages, reading user input, and formatting responses.
 */
public class Ui {
    /** A horizontal line used for visual separation in messages. */
    private static final String LINE = "-------------------------------------------------------------";

    /** Buffered reader for reading user input from the console. */
    private BufferedReader reader;

    /**
     * Constructs a {@code Ui} object and initializes the input reader.
     */
    public Ui() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Displays a horizontal line for visual separation.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Displays the welcome message when the chatbot starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm Fiona.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays an error message when there is an issue loading tasks from the file.
     */
    public void showLoadingError() {
        showLine();
        System.out.println("Error loading tasks from file. Starting with an empty task list.");
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
     * Displays a message to the user.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the goodbye message when the chatbot exits.
     */
    public void showBye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }
}
