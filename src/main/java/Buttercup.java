import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Buttercup {

    private static List<Task> tasks = new ArrayList<>();

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
                displayTasks();
            } else if (input.startsWith("mark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(5).trim());
                    mark(taskNumber);
                } catch (NumberFormatException e) {

                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());
                    unmark(taskNumber);
                } catch (NumberFormatException e) {

                }
            } else {
                addTask(input);
            }
            displayLine();
        }
    }

    public static void addTask(String input) {
        Task newTask = new Task(input);
        tasks.add(newTask);
        System.out.println("added: " + input);
    }

    public static void displayTasks() {
        System.out.println("Here are the tasks in your list:");
        int taskNumber = 1;
        for (Task task : tasks) {
            String str = String.format("%d.[%s] %s", taskNumber, task.getStatusIcon(), task);
            System.out.println(str);
            taskNumber++;
        }
    }

    public static void mark(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        String str = String.format("[%s] %s", task.getStatusIcon(), task);
        System.out.println(str);
    }

    public static void unmark(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        String str = String.format("[%s] %s", task.getStatusIcon(), task);
        System.out.println(str);
    }

    public static void displayLine() {
        System.out.println("____________________________________________________________");
    }
}
