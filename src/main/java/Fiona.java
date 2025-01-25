import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fiona {
    private static String line = "-------------------------------------------------------------";
    public static void main(String[] args) throws IOException, FionaException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        List<Task> taskList = new ArrayList<>();
        System.out.println(line+"\nHello! I'm Fiona.\nWhat can I do for you?\n"+line);

        while (true) {
            String[] inputs = br.readLine().trim().split("\\s+", 2);
            String action = inputs[0];

            if (action.equals("bye")) break;

            System.out.println(line);
            try {
                if (action.equals("todo")) {
                    if (inputs.length < 2 || inputs[1].trim().isEmpty()) {
                        throw new FionaException("The description of a todo cannot be empty.");
                    }
                    String name = inputs[1].trim();
                    Task t = new Todo(name);
                    taskList.add(t);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have "+taskList.size()+" task(s) in the list.");
    
                } else if (action.equals("deadline")) {
                    if (inputs.length < 2 || inputs[1].trim().isEmpty() || !inputs[1].contains("/by")) {
                        throw new FionaException("The description of a deadline must include a '/by' clause.");
                    }
                    String[] parts = inputs[1].split("/by", 2);
                    String name = parts[0].trim();
                    String deadline = parts[1].trim();
                    Task t = new Deadline(name, deadline);
                    taskList.add(t);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have "+taskList.size()+" task(s) in the list.");
    
                } else if (action.equals("event")) {
                    if (inputs.length < 2 || inputs[1].trim().isEmpty() || !inputs[1].contains("/from") || !inputs[1].contains("/to")) {
                        throw new FionaException("The description of an event must include '/from' and '/to' clauses.");
                    }
                    String[] fromSplit = inputs[1].split("/from", 2);
                    String name = fromSplit[0].trim();
                    
                    String[] toSplit = fromSplit[1].split("/to", 2);
                    String from = toSplit[0].trim(); 
                    String to = toSplit[1].trim(); 
                    Task t = new Event(name, from, to);
                    taskList.add(t);
                    System.out.println("Got it. I've added this task:");
                    System.out.println(t);
                    System.out.println("Now you have "+taskList.size()+" task(s) in the list.");
    
                } else if (action.equals("list")) {
                    for (int i=0; i<taskList.size(); ++i) {
                        System.out.println((i+1) + ". "+taskList.get(i));
                    }
    
                } else if (action.equals("mark")) {
                    if (inputs.length < 2) {
                        throw new FionaException("You must specify a valid task number to mark as done.");
                    }
                    int id = Integer.parseInt(inputs[1])-1;
                    taskList.get(id).setDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(taskList.get(id));
    
                } else if (action.equals("unmark")) {
                    if (inputs.length < 2) {
                        throw new FionaException("You must specify a valid task number to mark as not done yet.");
                    }
                    int id = Integer.parseInt(inputs[1])-1;
                    taskList.get(id).setUndone();
                    System.out.println("OK, I've marked this task as not done yet :");
                    System.out.println(taskList.get(id));
    
                } else if (action.equals("delete")) {
                    if (inputs.length < 2) {
                        throw new FionaException("You must specify a valid task number to delete.");
                    }
                    int id = Integer.parseInt(inputs[1])-1;
                    Task task = taskList.remove(id);
                    System.out.println("Noted. I've removed this task:\n"+task+"\nNow you have "+taskList.size()+" tasks in the list.");
                } else {
                    throw new FionaException("I'm sorry, but I don't know what that means :-(");
                }

            } catch (FionaException fE) {
                System.out.println(fE.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("The task number you specified must be a valid integer!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("The task number you specified does not exist!");
            } 
            System.out.println(line);
        }
        System.out.println(line+"\nBye. Hope to see you again soon!\n"+line);
    }
}