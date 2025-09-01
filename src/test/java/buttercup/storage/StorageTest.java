package buttercup.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import buttercup.exceptions.ButtercupException;
import buttercup.tasks.Task;
import buttercup.tasks.TaskList;
import buttercup.tasks.Todo;

public class StorageTest {

    private Path tasksFile;

    private String dir = "src/test/java/buttercup/data/";
    private String fileName = "tasks.txt";

    @BeforeEach
    void setUp() throws IOException {
        tasksFile = Paths.get(dir + fileName);
        writeLines(List.of());
    }

    private void writeLines(List<String> lines) throws IOException {
        Files.write(tasksFile, lines);
    }

    private List<String> readLines() throws IOException {
        return Files.readAllLines(tasksFile);
    }

    private Storage load() throws ButtercupException {
        return Storage.of(dir, fileName);
    }

    @Test
    void of_createsFileWhenMissing_success() throws Exception {
        Files.deleteIfExists(tasksFile);
        assertFalse(Files.exists(tasksFile));
        Storage storage = load();
        assertEquals(0, storage.getTasks().getSize());
    }

    @Test
    void loadTasks_parsesLines_success() throws Exception {
        writeLines(List.of(
                "T | 0 | read book",
                "D | 1 | return book | 2025-08-08T18:00",
                "E | 0 | project meeting | 2025-08-14T19:00 | 2025-08-14T22:00"
        ));

        Storage storage = load();
        TaskList tasks = storage.getTasks();
        assertEquals(3, tasks.getSize());

        assertEquals("read book", tasks.getTask(0).getDescription());
        assertFalse(tasks.getTask(0).isDone());

        assertEquals("return book", tasks.getTask(1).getDescription());
        assertTrue(tasks.getTask(1).isDone());

        assertEquals("project meeting", tasks.getTask(2).getDescription());
        assertFalse(tasks.getTask(2).isDone());
    }

    @Test
    void loadTasks_parsesInvalidLines_success() throws Exception {
        writeLines(List.of(
            "X | 0 | unknown type",
            "T | notABool | broken",
            "D | 1" // too few fields
        ));
        Storage storage = load();
        TaskList tasks = storage.getTasks();

        assertEquals(0, tasks.getSize());
    }

    @Test
    void addTask_addTaskToList_success() throws Exception {
        writeLines(List.of("T | 0 | seed"));
        Storage storage = load();

        int before = storage.getTasks().getSize();
        Todo t = new Todo("write tests");
        storage.addTask(t);

        assertEquals(before + 1, storage.getTasks().getSize());
        assertSame(t, storage.getTasks().getTask(1));

        // fresh load to confirm persistence
        Storage reloaded = load();
        assertEquals(before + 1, reloaded.getTasks().getSize());
        assertEquals("write tests", reloaded.getTasks().getTask(before).getDescription());
    }

    @Test
    void deleteTask_deleteTaskFromList_success() throws Exception {
        writeLines(List.of(
            "T | 0 | A",
            "T | 1 | B",
            "T | 0 | C"
        ));
        Storage storage = load();

        Task removed = storage.deleteTask(1);
        assertEquals("A", removed.getDescription());
        assertEquals(2, storage.getTasks().getSize());
        assertEquals("B", storage.getTasks().getTask(0).getDescription());
        assertEquals("C", storage.getTasks().getTask(1).getDescription());

        // fresh load to confirm persistence
        Storage reloaded = load();
        assertEquals(2, reloaded.getTasks().getSize());
        assertEquals("B", reloaded.getTasks().getTask(0).getDescription());
        assertEquals("C", reloaded.getTasks().getTask(1).getDescription());
    }

    @Test
    void deleteTask_deleteTaskOutOfRange_throwsException() throws Exception {
        writeLines(List.of("T | 0 | A"));
        Storage storage = load();

        int before = storage.getTasks().getSize();
        assertThrows(AssertionError.class, () -> {
            storage.deleteTask(2);
        });

        assertEquals(before, storage.getTasks().getSize());
        assertEquals(List.of("T | 0 | A"), readLines(), "File should remain unchanged");
    }

    @Test
    void setTaskCompletion_setCompletionPersists_success() throws Exception {
        writeLines(List.of(
                "T | 0 | A",
                "T | 0 | B"
        ));
        Storage storage = load();

        Task task = storage.getTasks().getTask(0);
        storage.setTaskCompletion(task, true);
        assertTrue(storage.getTasks().getTask(0).isDone());

        // fresh load to confirm persistence
        Storage reloaded = load();
        assertTrue(reloaded.getTasks().getTask(0).isDone());
    }

    @Test
    void setTaskCompletion_setNullTaskCompletion_throwsException() throws Exception {
        writeLines(List.of(" T | 0 | A"));
        Storage storage = load();

        assertThrows(AssertionError.class, () -> {
            storage.setTaskCompletion(null, true);
        });
    }
}
