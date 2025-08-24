package buttercup.storage;

import buttercup.exceptions.ButtercupException;
import buttercup.tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private TaskList tasks;
    private final Path file;

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

    private Storage(Path file) {
        this.file = file;
    }

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
                }
            } catch (Exception e) {
                System.out.println("Invalid task format, skipping and removing corrupted line: " + line);
//                displayLine();
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

    public TaskList getTasks() {
        return this.tasks;
    }

    public void setTaskCompletion(Task task, boolean isComplete) throws ButtercupException {
        if (isComplete) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        saveTasks(this.tasks.getTasks());
    }

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

    public void addTask(Task task) throws ButtercupException {
        this.tasks.addTask(task);
        saveTasks(this.tasks.getTasks());
    }

    public Task deleteTask(int index) throws ButtercupException {
        Task task = this.tasks.getTask(index - 1);
        this.tasks.removeTask(task);
        saveTasks(this.tasks.getTasks());
        return task;
    }
}
