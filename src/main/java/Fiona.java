import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Fiona {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line = "------------------------------------------";
        System.out.println(line+"\nHello! I'm Fiona.\nWhat can I do for you?\n"+line);
        while (true) {
            String input = br.readLine();
            System.out.println(line);
            if (input.equals("bye")) break;
            System.out.println(input+"\n"+line);
        }
        System.out.println("Bye. Hope to see you again soon!\n"+line);
    }
}