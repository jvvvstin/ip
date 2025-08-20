import java.time.LocalDateTime;
import utils.DateTimeFormatUtils;

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

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), DateTimeFormatUtils.formatDateTime(this.by));
    }

    @Override
    public String toFileString() {
        return String.format("D | %s | %s", super.toFileString(), this.by);
    }
}
