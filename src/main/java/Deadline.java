import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDate deadline;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public Deadline(String name, String deadline) throws FionaException {
        super(name);
        try {
            this.deadline = LocalDate.parse(deadline, STORAGE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date format for deadline. Please use yyyy-MM-dd.");
        }
    }

    public LocalDate getDeadline() {
        return this.deadline;
    }

    public String getByForStorage() {
        return this.deadline.format(STORAGE_FORMAT);
    }

    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[D][" + doneIndicator + "] " + super.getName() 
                + " (by: " + this.deadline.format(DISPLAY_FORMAT) + ")";
    }
}
