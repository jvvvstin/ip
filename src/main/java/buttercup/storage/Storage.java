package buttercup.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import buttercup.exceptions.ButtercupException;
import buttercup.tasks.Deadline;
import buttercup.tasks.Event;
import buttercup.tasks.Task;
import buttercup.tasks.TaskList;
import buttercup.tasks.Todo;

/**
 * Represents a file located in the user's directory. A <code>Storage</code>
 * object corresponds to file represented by two Strings <code>dir/fileName</code>
 */
public class Storage {
    private TaskList tasks;
    private final Path file;

    private Storage(Path file) {
        this.file = file;
    }

    /**
     * Returns a Storage object that represents the file used for saving.
     * The dir argument must specify a directory relative to the current
     * directory. The fileName argument represents the name of the file.
     * <p></p>
     * This method always returns, whether the folder or file specified
     * exists. If the folder or file does not exist, it will proceed to
     * create the folder and file specified. Else, it will return the
     * respective Storage object.
     * @param dir The folder path for where the file is located at.
     * @param fileName The name of the file located in the directory.
     * @return A Storage object for the file specified.
     * @see Storage
     */
    public static Storage of(String dir, String fileName) {
        Path path = Paths.get(dir);
        // check if file directory exists
        if (Files.notExists(path)) {
            try {
                // creates directory if it does not exist
                Files.createDirectory(path);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Path file = Paths.get(dir + fileName);
        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Storage storage = new Storage(file);
        storage.tasks = storage.loadTasks();
        return storage;
    }

    /**
     * Loads the user's list of tasks from their saved file and
     * returns a TaskList object that contains a list of all the
     * user's saved tasks. Any lines corrupted (invalid format)
     * will be removed from the saved file.
     * @return A TaskList object containing the user's tasks.
     * @see TaskList
     */
    public TaskList loadTasks() {
        List<String> lines = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        try {
            lines = Files.readAllLines(this.file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        for (String line : lines) {
            //  T | 0 | read book
            //  D | 0 | return book | 2025-08-08T18:00
            //  E | 0 | project meeting | 2025-08-14T19:00 | 2025-08-14T22:00
            try {
                String[] splitted = line.split(" \\| ");
                String type = splitted[0];
                if (!(splitted[1].equals("1") || splitted[1].equals("0"))) {
                    throw new ButtercupException("");
                }
                boolean isDone = splitted[1].equals("1");
                String description = splitted[2];

                switch (type) {
                case "T":
                    tasks.add(new Todo(description, isDone));
                    break;
                case "D":
                    tasks.add(new Deadline(description, isDone, LocalDateTime.parse(splitted[3])));
                    break;
                case "E":
                    tasks.add(new Event(description, isDone, LocalDateTime.parse(splitted[3]),
                            LocalDateTime.parse(splitted[4])));
                    break;
                default:
                    throw new ButtercupException("");
                }
            } catch (Exception e) {
                System.out.println("Invalid task format, skipping and removing corrupted line: " + line);
            }
        }

        // write all properly formatted lines to save file
        try {
            saveTasks(tasks);
        } catch (ButtercupException e) {
            System.out.println(e);
        }
        return new TaskList(tasks);
    }

    /**
     * Returns a TaskList object that contains a list of all the
     * user's saved tasks.
     * @return A TaskList object containing the user's tasks.
     * @see TaskList
     */
    public TaskList getTasks() {
        return this.tasks;
    }

    /**
     * Sets the isComplete value for the task being specified and
     * updates its value in the save file.
     * @param task The task which isComplete value is to be updated.
     * @param isComplete A boolean value representing whether the task
     *                   is completed.
     * @throws ButtercupException If there was an error writing to save file.
     * @see Task
     */
    public void setTaskCompletion(Task task, boolean isComplete) throws ButtercupException {
        if (isComplete) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        saveTasks(this.tasks.getTasks());
    }

    /**
     * Writes the list of tasks provided into the save file.
     * @param tasks The list of tasks to be saved.
     * @throws ButtercupException If there was an error writing to save file.
     */
    public void saveTasks(List<Task> tasks) throws ButtercupException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileString());
        }
        try {
            Files.write(this.file, lines);
        } catch (IOException e) {
            throw new ButtercupException("Error while writing buttercup.tasks to file: " + e.getMessage());
        }
    }

    /**
     * Adds the task specified to the current list of tasks
     * and saves the updated list of tasks to the save file.
     * @param task The task to be added.
     * @throws ButtercupException If there was an error writing to save file.
     */
    public void addTask(Task task) throws ButtercupException {
        this.tasks.addTask(task);
        saveTasks(this.tasks.getTasks());
    }

    /**
     * Removes the task at the specified index from the current
     * list of tasks and saves the updated list of tasks to the
     * save file.
     * @param index The index of the task to be removed.
     * @return The task previously at the specified position
     * @throws ButtercupException If there was an error writing to save file
     */
    public Task deleteTask(int index) throws ButtercupException {
        Task task = this.tasks.getTask(index - 1);
        this.tasks.removeTask(task);
        saveTasks(this.tasks.getTasks());
        return task;
    }
}
