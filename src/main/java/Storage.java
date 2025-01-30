import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
