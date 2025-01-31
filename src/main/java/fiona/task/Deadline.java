package fiona.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fiona.command.FionaException;

public class Deadline extends Task {
    private LocalDateTime deadline;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Deadline(String name, String deadline) throws FionaException {
        super(name);
        try {
            this.deadline = LocalDateTime.parse(deadline, STORAGE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format for deadline. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }
    }

    public LocalDateTime getDeadline() {
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
