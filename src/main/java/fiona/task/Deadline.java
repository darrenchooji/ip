package fiona.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fiona.command.FionaException;

/**
 * The {@code Deadline} class represents a task that has a specific due date and time.
 * It extends the {@code Task} class and includes a deadline.
 */
public class Deadline extends Task {
    /** Formatter for displaying dates in a user-friendly format. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /** Formatter for parsing and storing dates in a standardized format. */
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private LocalDateTime deadline;

    /**
     * Constructs a {@code Deadline} task with the specified description and due date.
     *
     * @param name The description of the deadline task.
     * @param deadline The due date and time in "yyyy-MM-dd HHmm" format.
     * @throws FionaException If the date-time format is invalid.
     */
    public Deadline(String name, String deadline) throws FionaException {
        super(name);
        try {
            this.deadline = LocalDateTime.parse(deadline, STORAGE_FORMAT);
            if (this.deadline.isBefore(LocalDateTime.now())) {
                throw new FionaException("The deadline cannot be before the today's date.");
            }
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format for deadline. "
                    + "Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }
    }

    /**
     * Returns the deadline of the task.
     *
     * @return The deadline as a {@code LocalDateTime} object.
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    /**
     * Returns the deadline formatted for storage.
     *
     * @return The deadline as a string in "yyyy-MM-dd HHmm" format.
     */
    public String getByForStorage() {
        return this.deadline.format(STORAGE_FORMAT);
    }

    /**
     * Returns a string representation of the deadline task, including its status, description,
     * and deadline.
     *
     * @return A formatted string representing the deadline task.
     */
    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[D][" + doneIndicator + "] " + super.getName()
                + " (by: " + this.deadline.format(DISPLAY_FORMAT) + ")";
    }
}
