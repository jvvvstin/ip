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
        Scanner scanner = new Scanner(System.in);

        while (!(input.equals("bye"))) {
            // Read user input
            input = scanner.nextLine().trim();
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
                    System.out.println("Invalid task number! Please enter in a valid task number e.g. mark 7.");
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());
                    unmark(taskNumber);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number! Please enter in a valid task number e.g. unmark 7.");
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
            } else if (input.startsWith("todo ") || input.startsWith("deadline ") ||
                    input.startsWith("event ")) {
                try {
                    addTask(input);
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
            } else if (input.equals("todo") || input.equals("deadline") ||
                    input.equals("event")) {
                try {
                    handleInvalidTasks(input);
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
            } else {
                System.out.println("I'm sorry, I do not recognise this command. Please try again.");
            }
            displayLine();
        }
    }

    private static void handleInvalidTasks(String input) throws ButtercupException {
        if (input.equals("todo")) {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try todo {description} instead.");
        } else if (input.equals("deadline")) {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try deadline {description} /by {deadline} instead.");
        } else {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try event {description} /from {start} /to {end} instead.");
        }
    }

    public static void addTask(String input) throws ButtercupException {
        Task newTask;
        if (input.startsWith("todo ")) {
            newTask = new Todo(input.substring(5).trim());
        } else if (input.startsWith("deadline ")) {
            input = input.substring(9);
            if (!input.contains("/by")) {
                throw new ButtercupException("Invalid format, deadline command should contain '/by' and be of the format deadline {description} /by {deadline} instead.");
            }
            String[] splitted = input.split("/by");
            if (splitted.length != 2) {
                throw new ButtercupException("Invalid format, deadline command should be of the format deadline {description} /by {deadline} instead.");
            }
            if (splitted[0].trim().isEmpty()) {
                throw new ButtercupException("Invalid format, deadline's description should not be empty and should be of the format deadline {description} /by {deadline} instead.");
            }
            if (splitted[1].trim().isEmpty()) {
                throw new ButtercupException("Invalid format, deadline's deadline should not be empty and should be of the format deadline {description} /by {deadline} instead.");
            }
            newTask = new Deadline(splitted[0].trim(), splitted[1].trim());
        } else {
            input = input.substring(6).trim();
            if (!input.contains("/from")) {
                throw new ButtercupException("Invalid format, deadline command should contain '/from' and be of the format event {description} /from {start} /to {end} instead.");
            }
            if (!input.contains("/to")) {
                throw new ButtercupException("Invalid format, deadline command should contain '/to' and be of the format event {description} /from {start} /to {end} instead.");
            }
            String[] splitted = input.split("/from");
            if (splitted.length != 2 || splitted[1].trim().isEmpty()) {
                throw new ButtercupException("Invalid format, event command should be of the format event {description} /from {start} /to {end} instead.");
            }
            String description = splitted[0].trim();
            if (description.isEmpty()) {
                throw new ButtercupException("Invalid format, event's description should not be empty and should be of the format event {description} /from {start} /to {end} instead.");
            }
            splitted = splitted[1].split("/to");
            if (splitted.length != 2) {
                throw new ButtercupException("Invalid format, event command should be of the format event {description} /from {start} /to {end} instead.");
            }
            String from = splitted[0].trim();
            String to = splitted[1].trim();
            if (from.isEmpty()) {
                throw new ButtercupException("Invalid format, event's start should not be empty and should be of the format event {description} /from {start} /to {end} instead.");
            }
            if (to.isEmpty()) {
                throw new ButtercupException("Invalid format, event's end should not be empty and should be of the format event {description} /from {start} /to {end} instead.");
            }
            newTask = new Event(description, from, to);
        }

        tasks.add(newTask);
        String str = String.format("Got it. I've added this task:\n" +
                                   "%s\n" +
                                   "Now you have %d %s in the list.",
                                   newTask, tasks.size(), tasks.size() == 1 ? "task" : "tasks");
        System.out.println(str);
    }

    public static void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks in the list.");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        int taskNumber = 1;
        for (Task task : tasks) {
            String str = String.format("%d. %s", taskNumber, task);
            System.out.println(str);
            taskNumber++;
        }
    }

    public static void mark(int taskNumber) throws ButtercupException {
        if (tasks.isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - " + tasks.size() + " e.g. mark 7.");
        }
        Task task = tasks.get(taskNumber - 1);
        task.markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    public static void unmark(int taskNumber) throws ButtercupException {
        if (tasks.isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - " + tasks.size() + " e.g. unmark 7.");
        }
        Task task = tasks.get(taskNumber - 1);
        task.markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    public static void displayLine() {
        System.out.println("____________________________________________________________");
    }
}
