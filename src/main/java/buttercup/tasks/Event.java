package buttercup.tasks;

import java.time.LocalDateTime;
import buttercup.utils.DateTimeFormatUtils;

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

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                DateTimeFormatUtils.formatDateTime(this.start),
                DateTimeFormatUtils.formatDateTime(this.end));
    }

    @Override
    public String toFileString() {
        return String.format("E | %s | %s | %s", super.toFileString(), this.start, this.end);
    }
}
