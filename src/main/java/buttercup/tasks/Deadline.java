package buttercup.tasks;

import java.time.LocalDateTime;
import buttercup.utils.DateTimeFormatUtils;

/**
 * Represents a Deadline task that is to be completed. A <code>Deadline</code>
 * object corresponds to a task that has a deadline for when the task has to
 * be completed.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public Deadline(String description, boolean isDone, LocalDateTime by) {
        super(description, isDone);
        this.by = by;
    }

    /**
     * Returns a <code>String</code> representation of the Deadline object.
     * @return A <code>String</code> representation of the Deadline object.
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), DateTimeFormatUtils.formatDateTime(this.by));
    }

    /**
     * Returns a <code>String</code> representation of the Deadline object
     * to be written in a save file.
     * @return A <code>String</code> representation of the Deadline object
     * to be written in a save file.
     */
    @Override
    public String toFileString() {
        return String.format("D | %s | %s", super.toFileString(), this.by);
    }
}
