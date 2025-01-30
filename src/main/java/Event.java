import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private LocalDate from;
    private LocalDate to;
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    public Event(String description, String from, String to) throws FionaException {
        super(description);
        try {
            this.from = LocalDate.parse(from, STORAGE_FORMAT);
            this.to = LocalDate.parse(to, STORAGE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new FionaException("Invalid date format for event. Please use yyyy-MM-dd.");
        }
    }

    public LocalDate getFrom() {
        return this.from;
    }

    public LocalDate getTo() {
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