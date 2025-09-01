package buttercup.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskListTest {
    private TaskList taskList;

    private List<Task> getSampleTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("todo"));
        tasks.add(new Deadline("deadline", LocalDateTime.now()));
        tasks.add(new Event("event", LocalDateTime.now(), LocalDateTime.now()));
        return tasks;
    }
    private List<Task> getSampleSimilarKeywordTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("return book"));
        tasks.add(new Deadline("test", LocalDateTime.now()));
        tasks.add(new Event("buy book on sale", LocalDateTime.now(), LocalDateTime.now()));
        return tasks;
    }

    @BeforeEach
    void setUp() {
        taskList = new TaskList(new ArrayList<>());
    }

    @Test
    void addTask_addsTaskToList_success() {
        int before = taskList.getSize();
        assertEquals(0, before);
        Task t1 = new Todo("test");
        taskList.addTask(t1);
        assertEquals(1, taskList.getSize());
    }

    @Test
    void addTask_addsTasksToList_success() {
        int before = taskList.getSize();
        assertEquals(0, before);
        Task t1 = new Todo("todo");
        Task t2 = new Deadline("deadline", LocalDateTime.now());
        Task t3 = new Event("event", LocalDateTime.now(), LocalDateTime.now());
        taskList.addTask(t1);
        assertEquals(1, taskList.getSize());
        taskList.addTask(t2);
        assertEquals(2, taskList.getSize());
        taskList.addTask(t3);
        assertEquals(3, taskList.getSize());
    }

    @Test
    void removeTask_removesTaskFromList_success() {
        int before = taskList.getSize();
        assertEquals(0, before);
        List<Task> tasks = getSampleTasks();
        taskList = new TaskList(tasks);
        assertEquals(3, taskList.getSize());
        Task remove = taskList.getTask(0);
        taskList.removeTask(remove);
        assertEquals(2, taskList.getSize());
        assertEquals("deadline", taskList.getTask(0).getDescription());
        assertEquals("event", taskList.getTask(1).getDescription());
    }

    @Test
    void getSize_returnsCorrectSize_success() {
        int before = taskList.getSize();
        assertEquals(0, before);
        taskList = new TaskList(getSampleTasks());
        assertEquals(3, taskList.getSize());
    }

    @Test
    void getTask_returnsCorrectTask_success() {
        taskList = new TaskList(getSampleTasks());
        Task task = taskList.getTask(0);
        assertEquals("todo", task.getDescription());
        task = taskList.getTask(1);
        assertEquals("deadline", task.getDescription());
        task = taskList.getTask(2);
        assertEquals("event", task.getDescription());
    }

    @Test
    void isEmpty_returnsFalse_success() {
        taskList = new TaskList(getSampleTasks());
        assertFalse(taskList.isEmpty());
    }

    @Test
    void isEmpty_returnsTrue_success() {
        taskList = new TaskList(new ArrayList<>());
        assertTrue(taskList.isEmpty());
    }

    @Test
    void filterByKeyword_returnsCorrectTask_success() {
        taskList = new TaskList(getSampleSimilarKeywordTasks());
        List<Task> tasks = taskList.filterByKeyword("book");
        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0).getDescription().contains("book"));
        assertTrue(tasks.get(1).getDescription().contains("book"));
    }

    @Test
    void filterByKeyword_noMatchingKeywordReturnsEmptyList_success() {
        taskList = new TaskList(getSampleSimilarKeywordTasks());
        List<Task> tasks = taskList.filterByKeyword("non-matching keyword");
        assertEquals(0, tasks.size());
    }
}
