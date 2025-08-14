import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Buttercup {

    private static List<String> tasks = new ArrayList<>();

    public static void main(String[] args) {
        displayLine();
        greet();
        displayLine();
        echo();
    }

    public static void greet() {
        String message = "Hello! I'm Buttercup\n" +
                         "What can I do for you?";
        System.out.println(message);
    }

    public static void exit() {
        String exitMessage = "Bye. Hope to see you again soon!";
        System.out.println(exitMessage);
    }

    public static void echo() {
        String input = "";

        while (!(input.equals("bye"))) {
            Scanner scanner = new Scanner(System.in);
            // Read user input
            input = scanner.nextLine();
            displayLine();
            if (input.equals("bye")) {
                exit();
            } else if (input.equals("list")) {
                // display list of tasks
            } else {
                addTask(input);
            }
            displayLine();
        }
    }

    public static void addTask(String input) {
        tasks.add(input);
        System.out.println("added: " + input);
    }

    public static void displayLine() {
        System.out.println("____________________________________________________________");
    }
}
