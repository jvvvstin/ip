package buttercup.tasks;

import java.time.LocalDateTime;
import buttercup.utils.DateTimeFormatUtils;

/**
 * Represents an Event task that is going to take place. An <code>Event</code>
 * object corresponds to an event that is going to take place and has a start
 * and end date for the event.
 */
public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public Event(String description, boolean isDone, LocalDateTime start, LocalDateTime end) {
        super(description, isDone);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a <code>String</code> representation of the Event object.
     * @return A <code>String</code> representation of the Event object.
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                DateTimeFormatUtils.formatDateTime(this.start),
                DateTimeFormatUtils.formatDateTime(this.end));
    }

    /**
     * Returns a <code>String</code> representation of the Event object
     * to be written in a save file.
     * @return A <code>String</code> representation of the Event object
     * to be written in a save file.
     */
    @Override
    public String toFileString() {
        return String.format("E | %s | %s | %s", super.toFileString(), this.start, this.end);
    }
}
