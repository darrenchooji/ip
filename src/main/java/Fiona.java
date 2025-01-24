import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Fiona {
    private static String line = "------------------------------------------------------";
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        List<Task> taskList = new ArrayList<>();
        System.out.println(line+"\nHello! I'm Fiona.\nWhat can I do for you?\n"+line);

        while (true) {
            String[] inputs = br.readLine().trim().split("\\s+", 2);
            String command = inputs[0];
            if (command.equals("bye")) break;
            
            System.out.println(line);
            if (command.equals("add")) {
                String taskName = inputs[1];
                Task task = new Task(taskName);
                taskList.add(task);
                System.out.println("added: "+taskName+"\n");
            } else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (Task task : taskList) {
                    String mark = task.getIsDone() ? "X" : " ";
                    System.out.println(task.getId()+". ["+mark+"] "+task.getTaskName());
                }
            } else if (command.equals("mark")) {
                int id = Integer.parseInt(inputs[1])-1;
                taskList.get(id).setIsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("[X] "+taskList.get(id).getTaskName());
            } else {    // unmark
                int id = Integer.parseInt(inputs[1])-1;
                taskList.get(id).setNotDone();;
                System.out.println("OK, I've marked this task as not doneyet :");
                System.out.println("[ ] "+taskList.get(id).getTaskName());
            }
            System.out.println(line);
        }
        System.out.println(line+"\nBye. Hope to see you again soon!\n"+line);
    }
}