import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Fiona {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line = "------------------------------------------";
        ArrayList<String> taskList = new ArrayList<>();
        System.out.println(line+"\nHello! I'm Fiona.\nWhat can I do for you?\n"+line);
        while (true) {
            String input = br.readLine();
            System.out.println(line);
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                for (int i=0; i<taskList.size(); ++i) {
                    int idx = i+1;
                    System.out.println(idx + ": "+taskList.get(i));
                }
                System.out.println(line);
            } else {
                taskList.add(input);
                System.out.println("added: "+input+"\n"+line);
            }
        }
        System.out.println("Bye. Hope to see you again soon!\n"+line);
    }
}