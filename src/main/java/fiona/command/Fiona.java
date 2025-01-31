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

public class Fiona {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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

                case UNKNOWN:
                default:
                    throw new FionaException("I'm sorry, but I don't know what that means :-(");
            }
        } catch (NumberFormatException e) {
            throw new FionaException("The task number you specified must be a valid integer!");
        }
    }

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

    private void addDeadline(String args) throws FionaException, IOException {
        if (args.isEmpty() || !args.contains("/by")) {
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

    private void addEvent(String args) throws FionaException, IOException {
        if (args.isEmpty() || !args.contains("/from") || !args.contains("/to")) {
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

    private void listTasks() {
        if (tasks.size() == 0) {
            ui.showMessage("Your task list is empty!");
        } else {
            ui.showMessage("Here are your existing tasks:");
            List<Task> taskList = tasks.getTasks();
            for (int i = 0; i < taskList.size(); ++i) {
                ui.showMessage((i + 1) + ". " + taskList.get(i));
            }
        }
    }

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
            ui.showMessage("No tasks found at " + targetDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")) + ".");
        } else {
            ui.showMessage("Here are the tasks at " + targetDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")) + ":");
            for (int i = 0; i < matchingTasks.size(); ++i) {
                ui.showMessage((i + 1) + ". " + matchingTasks.get(i));
            }
        }
    }

    public Fiona(Storage storage, TaskList tasks, Ui ui) {
        this.storage = storage;
        this.tasks = tasks;
        this.ui = ui;
    }

    public void processCommand(String fullCommand) throws FionaException, IOException {
        Command command = Parser.parse(fullCommand);
        handleCommand(command);
    }

    public TaskList getTaskList() {
        return tasks;
    }

    public static void main(String[] args) {
        new Fiona("./data/fiona.txt").run();
    }
}
