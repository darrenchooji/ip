import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Fiona {
    private static String LINE = "-------------------------------------------------------------";
    private static final String FILE_PATH = "./data/fiona.txt";
    public static void main(String[] args) throws IOException, FionaException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Storage storage = new Storage(FILE_PATH);
        List<Task> taskList = new ArrayList<>();
        try {
            taskList = storage.load();
            System.out.println(LINE);
            System.out.println("Hello! I'm Fiona.");

            if (taskList.isEmpty()) {
                System.out.println("Your task list is empty.");
            } else {
                System.out.println("Here are your existing tasks:");
                for (int i = 0; i < taskList.size(); ++i) {
                    System.out.println((i + 1) + ". " + taskList.get(i));
                }
            }
            System.out.println(LINE);
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
            System.out.println("Starting with an empty task list.");
            System.out.println(LINE);
        }
        
        System.out.println("What can I do for you?\n" + LINE);

        while (true) {
            String[] inputs = br.readLine().trim().split("\\s+", 2);
            Action action = Action.fromString(inputs[0]);
            if (action == Action.BYE) break;
        
            System.out.println(LINE);
            try {
                switch (action) {
                case TODO:
                    if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
                        throw new FionaException("The description of a todo cannot be empty.");
                    }
                    String name = inputs[1].trim();
                    Task t = new Todo(name);
                    taskList.add(t);
                    storage.save(taskList);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have " + taskList.size() + " task(s) in the list.");
                    break;
        
                case DEADLINE:
                    if (inputs.length < 2 || inputs[1].trim().isEmpty() || !inputs[1].contains("/by")) {
                        throw new FionaException("The description of a deadline must include a '/by' clause.");
                    }
                    String[] parts = inputs[1].split("/by", 2);
                    name = parts[0].trim();
                    String deadline = parts[1].trim();
                    t = new Deadline(name, deadline);
                    taskList.add(t);
                    storage.save(taskList);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have " + taskList.size() + " task(s) in the list.");
                    break;
        
                case EVENT:
                    if (inputs.length < 2 || inputs[1].trim().isEmpty() || !inputs[1].contains("/from") || !inputs[1].contains("/to")) {
                        throw new FionaException("The description of an event must include '/from' and '/to' clauses.");
                    }
                    String[] fromSplit = inputs[1].split("/from", 2);
                    name = fromSplit[0].trim();
                    String[] toSplit = fromSplit[1].split("/to", 2);
                    String from = toSplit[0].trim();
                    String to = toSplit[1].trim();
                    t = new Event(name, from, to);
                    taskList.add(t);
                    storage.save(taskList);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have " + taskList.size() + " task(s) in the list.");
                    break;
        
                case LIST:
                    if (taskList.isEmpty()) {
                        System.out.println("Your task list is empty!");
                    } else {
                        for (int i = 0; i < taskList.size(); ++i) {
                            System.out.println((i + 1) + ". " + taskList.get(i));
                        }
                    }
                    break;
        
                case MARK:
                    if (inputs.length < 2) {
                        throw new FionaException("You must specify a valid task number to mark as done.");
                    }
                    int id = Integer.parseInt(inputs[1]) - 1;
                    taskList.get(id).setDone();
                    storage.save(taskList);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(taskList.get(id));
                    break;
    
                case UNMARK:
                    if (inputs.length < 2) {
                        throw new FionaException("You must specify a valid task number to mark as not done yet.");
                    }
                    id = Integer.parseInt(inputs[1]) - 1;
                    taskList.get(id).setUndone();
                    storage.save(taskList);
                    System.out.println("OK, I've marked this task as not done yet :");
                    System.out.println(taskList.get(id));
                    break;
    
                case DELETE:
                    if (inputs.length < 2) {
                        throw new FionaException("You must specify a valid task number to delete.");
                    }
                    id = Integer.parseInt(inputs[1]) - 1;
                    Task task = taskList.remove(id);
                    storage.save(taskList);
                    System.out.println("Noted. I've removed this task:\n" + task + "\nNow you have " + taskList.size() + " tasks in the list.");
                    break;

                case FIND:
                    if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
                        throw new FionaException("You must specify a date-time in yyyy-MM-dd HHmm format to find tasks.");
                    }

                    String dateTimeStr = inputs[1].trim();
                    LocalDateTime targetDateTime;
                    try {
                        targetDateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                    } catch (DateTimeParseException e) {
                        throw new FionaException("Invalid date-time format. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
                    }

                    List<Task> matchingTasks = new ArrayList<>();

                    for (Task taskInList : taskList) {
                        if (taskInList instanceof Deadline) {
                            Deadline deadlineTask = (Deadline) taskInList;
                            if (deadlineTask.getDeadline().equals(targetDateTime)) {
                                matchingTasks.add(deadlineTask);
                            }
                        } else if (taskInList instanceof Event) {
                            Event eventTask = (Event) taskInList;
                            if (!eventTask.getFrom().isAfter(targetDateTime) && !eventTask.getTo().isBefore(targetDateTime)) {
                                matchingTasks.add(eventTask);
                            }
                        }
                    }

                    if (matchingTasks.isEmpty()) {
                        System.out.println("No tasks found at " + targetDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")) + ".");
                    } else {
                        System.out.println("Here are the tasks at " + targetDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm")) + ":");
                        for (int i = 0; i < matchingTasks.size(); ++i) {
                            System.out.println((i + 1) + ". " + matchingTasks.get(i));
                        }
                    }
                    break;
    
                case UNKNOWN:
                    throw new FionaException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (FionaException fE) {
                System.out.println(fE.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("The task number you specified must be a valid integer!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("The task number you specified does not exist!");
            }
            System.out.println(LINE);
        }
        System.out.println(LINE + "\nBye. Hope to see you again soon!\n" + LINE);
    }
}