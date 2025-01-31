package fiona.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import fiona.command.FionaException;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"); 

    public Event(String description, String from, String to) throws FionaException {
        super(description);
        try {
            this.from = LocalDateTime.parse(from, STORAGE_FORMAT);
            this.to = LocalDateTime.parse(to, STORAGE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date-time format for event. Please use yyyy-MM-dd HHmm (e.g., 2019-12-02 1800).");
        }
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    public String getFromForStorage() {
        return this.from.format(STORAGE_FORMAT);
    }

    public String getToForStorage() {
        return this.to.format(STORAGE_FORMAT);
    }

    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[E][" + doneIndicator + "] " + super.getName() 
                + " (from: " + this.from.format(DISPLAY_FORMAT) + " to: " 
                + this.to.format(DISPLAY_FORMAT) + ")";
    }
}
