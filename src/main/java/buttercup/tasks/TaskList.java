package buttercup.tasks;

import java.util.List;

/**
 * Represents a list of Tasks that the user has to complete. Each Task
 * could be a Todo, Deadline or Event object that the user has to
 * complete.
 */
public class TaskList {
    private List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds the specified <code>Task</code> object into the list
     * of tasks.
     * @param task The Task that is to be added into the list.
     * @see Task
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes the specified <code>Task</code> object from the list
     * of tasks.
     * @param task The task that is to be removed from the list.
     * @see Task
     */
    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    /**
     * Returns the total number of Tasks in the list.
     * @return An <code>int</code> representing the total number of
     *     tasks in the list.
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Returns the <code>Task</code> at the specified index from
     * the current list of tasks.
     * @param index The index of the task to be removed.
     * @return The task previously at the specified position.
     */
    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    /**
     * Returns the list of Tasks currently.
     * @return A list containing all the Tasks.
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Returns a <code>boolean</code> value representing whether the task
     * list is empty.
     * @return A <code>boolean</code> value representing whether the task
     *     list is empty.
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    /**
     * Returns a list of tasks which descriptions contains the specified keyword.
     * @param keyword The keyword that is used to filter the list of tasks.
     * @return A list of tasks which description contains the specified keyword.
     */
    public List<Task> filterByKeyword(String keyword) {
        return this.tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .toList();
    }

    /**
     * Returns a <code>String</code> representation of the TaskList object.
     * @return A <code>String</code> representation of the TaskList object.
     */
    @Override
    public String toString() {
        int taskNumber = 1;
        StringBuilder sb = new StringBuilder();
        for (Task task : this.tasks) {
            sb.append(String.format("%d. %s", taskNumber, task));
            taskNumber++;
            if (taskNumber <= this.tasks.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
