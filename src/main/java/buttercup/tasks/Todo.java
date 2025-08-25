package buttercup.tasks;

/**
 * Represents a Todo task that is to be completed.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    /**
     * Returns a <code>String</code> representation of the Todo task.
     * @return a <code>String</code> representation of the Todo task.
     */
    @Override
    public String toString() {
        return String.format("[T]%s", super.toString());
    }

    /**
     * Returns a <code>String</code> representation of the Todo object
     * to be written in a save file.
     * @return A <code>String</code> representation of the Todo object
     *     to be written in a save file.
     */
    @Override
    public String toFileString() {
        return String.format("T | %s", super.toFileString());
    }
}
