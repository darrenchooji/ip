package fiona.command;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fiona.task.Deadline;
import fiona.task.Event;
import fiona.task.Task;
import fiona.task.Todo;

/**
 * The {@code Storage} class is responsible for reading and writing tasks to a file.
 * It allows loading tasks from a file at startup and saving tasks whenever changes occur.
 */
public class Storage {
    /** The file path where tasks are stored. */
    private final String filePath;

    /**
     * Constructs a {@code Storage} object with the specified file path.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return A list of tasks loaded from the file.
     * @throws IOException If an error occurs while reading the file.
     */
    public List<Task> load() throws IOException {
        List<Task> taskList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return taskList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line);
                if (task != null) {
                    taskList.add(task);
                }
            }
        }
        return taskList;
    }

    /**
     * Saves the given list of tasks to the storage file.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void save(List<Task> tasks) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(serializeTask(task));
                writer.newLine();
            }
        }
    }

    /**
     * Parses a task from a line of text stored in the file.
     *
     * @param line The line representing a task in the storage file.
     * @return The corresponding {@code Task} object, or {@code null} if the line is invalid.
     */
    private Task parseTask(String line) {
        System.out.println("Parsing line: " + line); // Debug statement
        String[] parts = line.split(" \\| ");

        if (parts.length < 3) {
            System.out.println("Skipping malformed line (insufficient parts): " + line);
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        try {
            switch (type) {
            case "T":
                Task todo = new Todo(description);
                if (isDone) todo.setDone();
                return todo;
            case "D":
                if (parts.length < 4) {
                    System.out.println("Skipping malformed Deadline task (missing 'by' field): " + line);
                    return null;
                }
                String deadline = parts[3];
                Task deadlineTask = new Deadline(description, deadline);
                if (isDone) deadlineTask.setDone();
                return deadlineTask;
            case "E":
                if (parts.length < 5) {
                    System.out.println("Skipping malformed Event task (missing 'from' or 'to' fields): " + line);
                    return null;
                }
                String from = parts[3];
                String to = parts[4];
                Task event = new Event(description, from, to);
                if (isDone) event.setDone();
                return event;
            default:
                System.out.println("Unknown task type: " + type + " in line: " + line);
                return null;
            }
        } catch (FionaException e) {
            System.out.println("Error parsing task: " + e.getMessage() + " in line: " + line);
            return null;
        }
    }

    /**
     * Serializes a task into a string format suitable for storage in a file.
     *
     * @param task The task to serialize.
     * @return A string representation of the task for storage.
     */
    private String serializeTask(Task task) {
        StringBuilder sb = new StringBuilder();
        if (task instanceof Todo) {
            sb.append("T | ");
        } else if (task instanceof Deadline) {
            sb.append("D | ");
        } else if (task instanceof Event) {
            sb.append("E | ");
        }
        sb.append(task.getIsDone() ? "1 | " : "0 | ");
        sb.append(task.getName());

        if (task instanceof Deadline) {
            sb.append(" | ").append(((Deadline) task).getByForStorage());
        } else if (task instanceof Event) {
            sb.append(" | ").append(((Event) task).getFromForStorage());
            sb.append(" | ").append(((Event) task).getToForStorage());
        }

        return sb.toString();
    }
}
