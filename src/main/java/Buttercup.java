import java.util.Scanner;

public class Buttercup {
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
            } else {
                System.out.println(input);
            }
            displayLine();
        }
    }

    public static void displayLine() {
        System.out.println("____________________________________________________________");
    }
}
