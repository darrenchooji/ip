import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Fiona {
    private static String line = "-------------------------------------------------------------";
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        List<Task> taskList = new ArrayList<>();
        System.out.println(line+"\nHello! I'm Fiona.\nWhat can I do for you?\n"+line);

        while (true) {
            String[] inputs = br.readLine().trim().split("\\s+", 2);
            String action = inputs[0];

            if (action.equals("bye")) break;

            System.out.println(line);
            if (action.equals("todo")) {
                String name = inputs[1].trim();
                Task t = new Todo(name);
                taskList.add(t);
                System.out.println("Got it. I've added this task:");
                System.out.println(t);
                System.out.println("Now you have "+taskList.size()+" task(s) in the list.");

            } else if (action.equals("deadline")) {
                String[] parts = inputs[1].split("/by", 2);
                String name = parts[0].trim();
                String deadline = parts[1].trim();
                Task t = new Deadline(name, deadline);
                taskList.add(t);
                System.out.println("Got it. I've added this task:");
                System.out.println(t);
                System.out.println("Now you have "+taskList.size()+" task(s) in the list.");

            } else if (action.equals("event")) {
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
                for (Task t : taskList) {
                    System.out.println(t.getId() + ". "+t);
                }

            } else if (action.equals("mark")) {
                int id = Integer.parseInt(inputs[1])-1;
                taskList.get(id).setDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(taskList.get(id));

            } else {
                int id = Integer.parseInt(inputs[1])-1;
                taskList.get(id).setUndone();
                System.out.println("OK, I've marked this task as not done yet :");
                System.out.println(taskList.get(id));

            }
            System.out.println(line);
        }
        System.out.println(line+"\nBye. Hope to see you again soon!\n"+line);
    }
}