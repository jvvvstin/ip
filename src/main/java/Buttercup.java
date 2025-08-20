import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Buttercup {

    private static List<Task> tasks = new ArrayList<>();
    private static final String TASKS_FILEPATH = "data";
    private static final String TASKS_FILENAME = TASKS_FILEPATH + "/tasks.txt";

    public static void main(String[] args) {
        setup();
        displayLine();
        greet();
        displayLine();
        loadTasks();
        echo();
    }

    private static void loadTasks() {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(TASKS_FILENAME));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        for (String line : lines) {
            // format: T | 1 | read book
            //         D | 0 | return book | June 6th
            //         E | 0 | project meeting | Aug 6th 2pm | 4pm
            try {
                String[] splitted = line.split(" \\| ");
                String type = splitted[0];
                boolean isDone = splitted[1].equals("1");
                String description = splitted[2];

                switch (type) {
                case "T":
                    Buttercup.tasks.add(new Todo(description, isDone));
                    break;
                case "D":
                    Buttercup.tasks.add(new Deadline(description, isDone, splitted[3]));
                    break;
                case "E":
                    Buttercup.tasks.add(new Event(description, isDone, splitted[3], splitted[4]));
                    break;
                }
            } catch (Exception e) {
                System.out.println("Invalid task format, skipping and removing corrupted line: " + line);
                displayLine();
            }
        }

        // write all properly formatted lines to save file
        try {
            saveTasks(Buttercup.tasks);
        } catch (ButtercupException e) {
            System.out.println(e);
        }
    }

    private static void saveTasks(List<Task> tasks) throws ButtercupException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }
        try {
            Files.write(Paths.get(TASKS_FILENAME), lines);
        } catch (IOException e) {
            throw new ButtercupException("Error while writing tasks to file: " + e.getMessage());
        }
    }

    private static void setup() {
        Path path = Paths.get(TASKS_FILEPATH);
        // check if file directory exists
        if (Files.notExists(path)) {
            try {
                // creates directory if it does not exist
                Files.createDirectory(path);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Path file = Paths.get(TASKS_FILENAME);
        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
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
            Command command = null;
            try {
                command = Command.getCommand(input.split(" ")[0]);
            } catch (ButtercupException e) {
                System.out.println(e);
                displayLine();
                continue;
            }
            switch (command) {
            case BYE:
                exit();
                break;
            case LIST:
                displayTasks();
                break;
            case MARK:
                try {
                    int taskNumber = Integer.parseInt(input.substring(5).trim());
                    mark(taskNumber);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number! Please enter in a valid task number e.g. mark 7.");
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
                break;
            case UNMARK:
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());
                    unmark(taskNumber);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number! Please enter in a valid task number e.g. unmark 7.");
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
                break;
            case TODO:
            case DEADLINE:
            case EVENT:
                try {
                    addTask(input);
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
                break;
            case DELETE:
                try {
                    int taskNumber = Integer.parseInt(input.substring(7).trim());
                    deleteTask(taskNumber);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid task number! Please enter in a valid task number e.g. delete 7.");
                } catch (ButtercupException e) {
                    System.out.println(e);
                }
                break;
            default:
                continue;
            }
            displayLine();
        }
    }

    private static void deleteTask(int taskNumber) throws ButtercupException {
        if (tasks.isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - " + tasks.size() + " e.g. delete 7.");
        }
        Task task = tasks.remove(taskNumber - 1);
        saveTasks(Buttercup.tasks);
        System.out.println("Noted! I've removed this task:");
        System.out.println(task);
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
        Task newTask = null;
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
        } else if (input.startsWith("event ")) {
            input = input.substring(6).trim();
            if (!input.contains("/from")) {
                throw new ButtercupException("Invalid format, event command should contain '/from' and be of the format event {description} /from {start} /to {end} instead.");
            }
            if (!input.contains("/to")) {
                throw new ButtercupException("Invalid format, event command should contain '/to' and be of the format event {description} /from {start} /to {end} instead.");
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
        } else {
            handleInvalidTasks(input);
            return;
        }

        tasks.add(newTask);
        try {
            writeToFile(TASKS_FILENAME, newTask.toFileString());
        } catch (ButtercupException e) {
            System.out.println(e);
        }

        String str = String.format("Got it. I've added this task:\n" +
                                   "%s\n" +
                                   "Now you have %d %s in the list.",
                                   newTask, tasks.size(), tasks.size() == 1 ? "task" : "tasks");
        System.out.println(str);
    }

    private static void writeToFile(String filepath, String taskToAdd) throws ButtercupException {
        try {
            FileWriter fw = new FileWriter(filepath, true);
            fw.write(taskToAdd + "\n");
            fw.close();
        } catch (IOException e) {
            throw new ButtercupException("Error while writing tasks to file: " + e.getMessage());
        }
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
        saveTasks(Buttercup.tasks);
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
        saveTasks(Buttercup.tasks);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    public static void displayLine() {
        System.out.println("____________________________________________________________");
    }
}
