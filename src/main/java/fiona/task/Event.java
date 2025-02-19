package fiona.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fiona.command.FionaException;

/**
 * The {@code Event} class represents a task that occurs within a specific time frame.
 * It extends the {@code Task} class and includes a start time ("from") and an end time ("to").
 */
public class Event extends Task {
    /** Formatter for displaying dates in a user-friendly format. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /** Formatter for parsing and storing dates in a standardized format. */
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructs an {@code Event} task with the specified description, start time, and end time.
     *
     * @param description The description of the event.
     * @param from The start time of the event in "yyyy-MM-dd HHmm" format.
     * @param to The end time of the event in "yyyy-MM-dd HHmm" format.
     * @throws FionaException If the date-time format is invalid.
     */
    public Event(String description, String from, String to) throws FionaException {
        super(description);
        try {
            this.from = LocalDateTime.parse(from, STORAGE_FORMAT);
            this.to = LocalDateTime.parse(to, STORAGE_FORMAT);
            if (this.from.isAfter(this.to)) {
                throw new FionaException("Start date time cannot be after end date time.");
            }
            if (this.to.isBefore(LocalDateTime.now())) {
                throw new FionaException("End date cannot be before today's date.");
            }
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format for event. "
                    + "Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }
    }

    /**
     * Returns the start time of the event.
     *
     * @return The start time as a {@code LocalDateTime} object.
     */
    public LocalDateTime getFrom() {
        return this.from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The end time as a {@code LocalDateTime} object.
     */
    public LocalDateTime getTo() {
        return this.to;
    }

    /**
     * Returns the start time formatted for storage.
     *
     * @return The start time as a string in "yyyy-MM-dd HHmm" format.
     */
    public String getFromForStorage() {
        return this.from.format(STORAGE_FORMAT);
    }

    /**
     * Returns the end time formatted for storage.
     *
     * @return The end time as a string in "yyyy-MM-dd HHmm" format.
     */
    public String getToForStorage() {
        return this.to.format(STORAGE_FORMAT);
    }

    /**
     * Returns a string representation of the event task, including its status, description,
     * start time, and end time.
     *
     * @return A formatted string representing the event task.
     */
    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[E][" + doneIndicator + "] " + super.getName()
                + " (from: " + this.from.format(DISPLAY_FORMAT) + " to: "
                + this.to.format(DISPLAY_FORMAT) + ")";
    }
}
