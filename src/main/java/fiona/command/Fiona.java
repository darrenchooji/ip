package fiona.command;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            tasks.purgeOverdueTasks();
            storage.save(tasks.getTasks());
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
     * @throws FionaException If any command format is invalid.
     * @throws IOException    If there is an error accessing the specified file.
     */
    private void handleCommand(Command command) throws FionaException, IOException {
        try {
            runAction(command.getAction(), command.getArgs());
        } catch (NumberFormatException e) {
            throw new FionaException("The task number you specified must be a valid integer!");
        }
    }

    /**
     * Executes the appropriate action based on the given Action and args.
     *
     * @param action The Action to perform (e.g., TODO, LIST, etc.).
     * @param args   The arguments passed to that action.
     * @throws FionaException If the command format is invalid.
     * @throws IOException    If there is an error accessing the file.
     */
    private void runAction(Action action, String args) throws FionaException, IOException {
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
        default:
            throw new FionaException("I'm sorry, but I don't know what that means :-(");
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

    private void findTasks(String args) throws FionaException {
        if (args.isEmpty()) {
            throw new FionaException("You must specify a date or date-time in the correct format to find tasks.");
        }
        LocalDateTime start;
        LocalDateTime end;
        if (isDateOnly(args)) {
            LocalDate date = parseLocalDate(args);
            start = date.atStartOfDay();
            end = date.atTime(23, 59);
        } else if (isDateTime(args)) {
            LocalDateTime dateTime = parseLocalDateTime(args);
            start = dateTime;
            end = dateTime;
        } else {
            throw new FionaException("Invalid date/time format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm.");
        }

        List<Task> matchingTasks = tasks.getTasks().stream()
                .filter(task -> isTaskInRange(task, start, end))
                .toList();

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No tasks found matching the date/date-time you provided");
        } else {
            ui.showMessage("Here are the matching tasks:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                ui.showMessage((i + 1) + ". " + matchingTasks.get(i));
            }
        }
    }

    private boolean isDateOnly(String input) {
        return input.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isDateTime(String input) {
        return input.matches("\\d{4}-\\d{2}-\\d{2}\\s+\\d{4}");
    }

    private LocalDate parseLocalDate(String dateStr) throws FionaException {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date format. Please use yyyy-MM-dd (e.g., 2025-02-13).");
        }
    }

    private LocalDateTime parseLocalDateTime(String dateTimeStr) throws FionaException {
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format. Please use yyyy-MM-dd HHmm (e.g., 2025-02-13 1800).");
        }
    }

    private boolean isTaskInRange(Task task, LocalDateTime start, LocalDateTime end) {
        if (task instanceof Deadline) {
            Deadline deadlineTask = (Deadline) task;
            LocalDateTime dl = deadlineTask.getDeadline();
            return !dl.isBefore(start) && !dl.isAfter(end);
        } else if (task instanceof Event) {
            Event eventTask = (Event) task;
            return !eventTask.getTo().isBefore(start) && !eventTask.getFrom().isAfter(end);
        }
        return false;
    }

    /**
     * Parses a date-time string in "yyyy-MM-dd HHmm" format.
     *
     * @param dateTimeStr The date-time string to parse.
     * @return The corresponding LocalDateTime object.
     * @throws FionaException If the string is not in the correct format.
     */
    private LocalDateTime parseDate(String dateTimeStr) throws FionaException {
        try {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }
    }

    /**
     * Determines if a given task matches the specified date-time.
     * For a Deadline, we check equality.
     * For an Event, we check whether the date-time falls within the event's range.
     *
     * @param task           The task to check.
     * @param targetDateTime The date-time we want to match.
     * @return True if the task matches, false otherwise.
     */
    private boolean isTaskMatchingDate(Task task, LocalDateTime targetDateTime) {
        if (task instanceof Deadline) {
            Deadline deadlineTask = (Deadline) task;
            return deadlineTask.getDeadline().equals(targetDateTime);
        } else if (task instanceof Event) {
            Event eventTask = (Event) task;
            return !eventTask.getFrom().isAfter(targetDateTime)
                    && !eventTask.getTo().isBefore(targetDateTime);
        }
        return false;
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
