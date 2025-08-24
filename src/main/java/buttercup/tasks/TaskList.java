package buttercup.tasks;

import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
    }

    public int getSize() {
        return this.tasks.size();
    }

    public Task getTask(int index) {
        return this.tasks.get(index);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    public List<Task> filterByKeyword(String keyword) {
        return this.tasks.stream()
                .filter(task -> task.getDescription().equals(keyword))
                .toList();
    }

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
