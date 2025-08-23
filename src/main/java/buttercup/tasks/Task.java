package buttercup.tasks;

/**
 * Represents a task that is to be completed. A <code>Task</code> object
 * corresponds to a task that contains a description and whether it is
 * completed or not.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns a <code>String</code> representation of whether the task is
     * completed or not.
     * @return A <code>String</code> representation of whether the task is
     * completed or not
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Updates the task to be completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Updates the task to be incomplete.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the description of the task.
     * @return A <code>String</code> representing the description
     * of the task.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns whether the task is completed or not.
     * @return A <code>boolean</code> value representing whether the
     * task is completed or not.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns a <code>String</code> representation of the Task object.
     * @return A <code>String</code> representation of the Task object.
     */
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), this.description);
    }

    /**
     * Returns a <code>String</code> representation of the Task object
     * to be written in a save file.
     * @return A <code>String</code> representation of the Task object
     * to be written in a save file.
     */
    public String toFileString() {
        return String.format("%s | %s", this.isDone ? "1" : "0", this.description);
    }
}
