import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ui {
    private static final String LINE = "-------------------------------------------------------------";
    private BufferedReader reader;

    public Ui() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm Fiona.");
        System.out.println("What can I do for you?");
        showLine();
    }

    public void showLoadingError() {
        showLine();
        System.out.println("Error loading tasks from file. Starting with an empty task list.");
        showLine();
    }

    public String readCommand() throws IOException {
        return reader.readLine();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showBye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }
}
