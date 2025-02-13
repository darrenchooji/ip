package fiona.command;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import fiona.task.Deadline;
import fiona.task.Event;
import fiona.task.Task;
import fiona.task.Todo;
import javafx.application.Platform;

/**
 * The {@code Fiona} class represents a chatbot that helps users manage tasks.
 * It supports adding, listing, marking, unmarking, deleting, and finding tasks.
 * Tasks can be of type {@code Todo}, {@code Deadline}, or {@code Event}.
 */
public class Fiona {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a {@code Fiona} chatbot that loads tasks from the specified file.
     *
     * @param filePath The file path where tasks are stored.
     */
    public Fiona(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            ui.showWelcome();
            listTasks();
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
        ui.showLine();
    }

    /**
     * Constructs a {@code Fiona} chatbot with the given {@code Storage}, {@code TaskList}, and {@code Ui} components.
     * This constructor is used for testing.
     *
     * @param storage The storage system for saving and loading tasks.
     * @param tasks   The task list containing the user's tasks.
     * @param ui      The user interface for interaction.
     */
    public Fiona(Storage storage, TaskList tasks, Ui ui) {
        this.storage = storage;
        this.tasks = tasks;
        this.ui = ui;
    }
    /**
     * Runs the chatbot. It continuously processes user commands until the "bye" command is given.
     */
    public void run() {
        while (true) {
            try {
                String fullCommand = ui.readCommand();
                Command command = Parser.parse(fullCommand);
                Action action = command.getAction();

                if (action == Action.BYE) {
                    ui.showBye();
                    break;
                }

                ui.showLine();
                handleCommand(command);
                ui.showLine();
            } catch (IOException e) {
                ui.showMessage("Error reading input: " + e.getMessage());
            } catch (FionaException e) {
                ui.showMessage(e.getMessage());
                ui.showLine();
            }
        }
    }

    /**
     * Handles the given user command.
     *
     * @param command The command to process.
     * @throws FionaException If any of the command format is invalid.
     * @throws IOException    If there is an error accessing the specified file.
     */
    private void handleCommand(Command command) throws FionaException, IOException {
        Action action = command.getAction();
        String args = command.getArgs();

        try {
            switch (action) {
            case TODO:
                addTodo(args);
                break;

            case DEADLINE:
                addDeadline(args);
                break;

            case EVENT:
                addEvent(args);
                break;

            case LIST:
                listTasks();
                break;

            case MARK:
                markTask(args);
                break;

            case UNMARK:
                unmarkTask(args);
                break;

            case DELETE:
                deleteTask(args);
                break;

            case FIND:
                findTasks(args);
                break;

            case FIND_KEYWORD:
                findTasksByKeyword(args);
                break;

            case UNKNOWN:
            default:
                throw new FionaException("I'm sorry, but I don't know what that means :-(");
            }
        } catch (NumberFormatException e) {
            throw new FionaException("The task number you specified must be a valid integer!");
        }
    }

    /**
     * Adds a new {@code Todo} task.
     *
     * @param args The description of the task.
     * @throws FionaException If the description is empty.
     * @throws IOException    If there is an error saving to the specified file.
     */
    private void addTodo(String args) throws FionaException, IOException {
        if (args.isEmpty()) {
            throw new FionaException("The description of a todo cannot be empty.");
        }
        Task todo = new Todo(args);
        tasks.add(todo);
        storage.save(tasks.getTasks());
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage(todo.toString());
        ui.showMessage("Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Adds a new {@code Deadline} task.
     *
     * @param args The task description and deadline, formatted as "description /by deadline".
     * @throws FionaException If the input format is invalid.
     * @throws IOException    If there is an error saving to the specified file.
     */
    private void addDeadline(String args) throws FionaException, IOException {
        if (!args.contains("/by")) {
            throw new FionaException("The description of a deadline must include a '/by' clause.");
        }
        String[] parts = args.split("/by", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new FionaException("Invalid format for deadline. Use: deadline <description> /by <deadline>");
        }
        String description = parts[0].trim();
        String deadline = parts[1].trim();
        Task deadlineTask = new Deadline(description, deadline);
        tasks.add(deadlineTask);
        storage.save(tasks.getTasks());
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage(deadlineTask.toString());
        ui.showMessage("Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Adds a new {@code Event} task.
     *
     * @param args The task description, from, and to, formatted as "description \from from \to to".
     * @throws FionaException If the input format is invalid.
     * @throws IOException    If there is an error saving to the specified file.
     */
    private void addEvent(String args) throws FionaException, IOException {
        if (!args.contains("/from") || !args.contains("/to")) {
            throw new FionaException("The description of an event must include '/from' and '/to' clauses.");
        }
        String[] fromSplit = args.split("/from", 2);
        if (fromSplit.length < 2 || fromSplit[0].trim().isEmpty() || fromSplit[1].trim().isEmpty()) {
            throw new FionaException("Invalid format for event. Use: event <description> /from <start> /to <end>");
        }
        String description = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split("/to", 2);
        if (toSplit.length < 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new FionaException("Invalid format for event. Use: event <description> /from <start> /to <end>");
        }
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();
        Task event = new Event(description, from, to);
        tasks.add(event);
        storage.save(tasks.getTasks());
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage(event.toString());
        ui.showMessage("Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Lists all tasks currently stored.
     */
    private void listTasks() {
        if (tasks.size() == 0) {
            ui.showMessage("Your task list is empty!");
        } else {
            ui.showMessage("Here are your existing tasks:");
            List<Task> taskList = tasks.getTasks();
            java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger(1);
            taskList.forEach(task -> {
                ui.showMessage(counter.getAndIncrement() + ". " + task);
            });
        }
    }

    /**
     * Marks a task as completed.
     *
     * @param args The task number to mark.
     * @throws FionaException If the task number is invalid.
     * @throws IOException    If there is an error saving to specified file.
     */
    private void markTask(String args) throws FionaException, IOException {
        if (args.isEmpty()) {
            throw new FionaException("You must specify a valid task number to mark as done.");
        }
        int id = Integer.parseInt(args) - 1;
        Task task = tasks.mark(id);
        storage.save(tasks.getTasks());
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage(task.toString());
    }

    /**
     * Unmarks a task as not completed yet.
     *
     * @param args The task number to mark.
     * @throws FionaException If the task number is invalid.
     * @throws IOException    If there is an error saving to specified file.
     */
    private void unmarkTask(String args) throws FionaException, IOException {
        if (args.isEmpty()) {
            throw new FionaException("You must specify a valid task number to mark as not done yet.");
        }
        int id = Integer.parseInt(args) - 1;
        Task task = tasks.unmark(id);
        storage.save(tasks.getTasks());
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage(task.toString());
    }

    /**
     * Deletes a task from the list.
     *
     * @param args The task number to delete.
     * @throws FionaException If the task number is invalid.
     * @throws IOException    If there is an error saving to specified file.
     */
    private void deleteTask(String args) throws FionaException, IOException {
        if (args.isEmpty()) {
            throw new FionaException("You must specify a valid task number to delete.");
        }
        int id = Integer.parseInt(args) - 1;
        Task task = tasks.getTasks().remove(id);
        storage.save(tasks.getTasks());
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage(task.toString());
        ui.showMessage("Now you have " + tasks.size() + " task(s) in the list.");
    }

    /**
     * Finds tasks based on a specific date and time.
     *
     * @param args The date-time string in the format "yyyy-MM-dd HHmm".
     * @throws FionaException If the format is incorrect or no tasks are found.
     */
    private void findTasks(String args) throws FionaException {
        if (args.isEmpty()) {
            throw new FionaException("You must specify a date-time in yyyy-MM-dd HHmm format to find tasks.");
        }

        String dateTimeStr = args;
        LocalDateTime targetDateTime;
        try {
            targetDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }

        List<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks.getTasks()) {
            if (task instanceof Deadline) {
                Deadline deadlineTask = (Deadline) task;
                if (deadlineTask.getDeadline().equals(targetDateTime)) {
                    matchingTasks.add(deadlineTask);
                }
            } else if (task instanceof Event) {
                Event eventTask = (Event) task;
                if (!eventTask.getFrom().isAfter(targetDateTime) && !eventTask.getTo().isBefore(targetDateTime)) {
                    matchingTasks.add(eventTask);
                }
            }
        }

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No tasks found at " + targetDateTime.format(DateTimeFormatter
                    .ofPattern("MMM dd yyyy HH:mm")) + ".");
        } else {
            ui.showMessage("Here are the tasks at " + targetDateTime.format(DateTimeFormatter
                    .ofPattern("MMM dd yyyy HH:mm")) + ":");
            for (int i = 0; i < matchingTasks.size(); ++i) {
                ui.showMessage((i + 1) + ". " + matchingTasks.get(i));
            }
        }
    }

    /**
     * Finds tasks containing the given keyword in their description.
     *
     * @param keyword The keyword to search for.
     * @throws FionaException If the keyword is empty.
     */
    private void findTasksByKeyword(String keyword) throws FionaException {
        if (keyword.isEmpty()) {
            throw new FionaException("You must specify a keyword to search for.");
        }

        List<Task> matchingTasks = tasks.getTasks().stream()
                .filter(t -> t.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        if (matchingTasks.isEmpty()) {
            ui.showMessage("No tasks found containing the keyword: " + keyword);
        } else {
            ui.showMessage("Here are the tasks containing \"" + keyword + "\":");
            for (int i = 0; i < matchingTasks.size(); ++i) {
                ui.showMessage((i + 1) + ". " + matchingTasks.get(i));
            }
        }
    }

    /**
     * Processes a single user command. Used for testing.
     *
     * @param fullCommand The full command entered by the user.
     * @throws FionaException If the command format is invalid.
     * @throws IOException    If there is an error accessing storage.
     */
    public void processCommand(String fullCommand) throws FionaException, IOException {
        Command command = Parser.parse(fullCommand);
        handleCommand(command);
    }

    /**
     * The main entry point of the Fiona chatbot.
     *
     * @param args Command-line arguments.
     */
    public static void main(String... args) {
        new Fiona("./data/fiona.txt").run();
    }

    public String getResponse(String input) {
        try {
            // Parse the command first
            Command command = Parser.parse(input);
            if (command.getAction() == Action.BYE) {
                ui.showBye();
                String farewell = ui.getMessage();
                // Exit the JavaFX application
                Platform.exit();
                return farewell;
            } else {
                handleCommand(command);
            }
        } catch (FionaException | IOException e) {
            ui.showMessage(e.getMessage());
        }
        return ui.getMessage();
    }


    public String getWelcomeMessage() {
        return ui.getMessage();
    }
}
