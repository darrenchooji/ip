package fiona.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fiona.command.FionaException;

public class EventTest {

    @Test
    void constructor_validFormat_success() {
        Assertions.assertDoesNotThrow(() -> {
            Event event = new Event("Team meeting", "2027-02-20 1400", "2027-02-20 1500");
            String eventStr = event.toString();
            Assertions.assertTrue(eventStr.contains("Team meeting"));
            Assertions.assertTrue(eventStr.contains("from:"));
            Assertions.assertTrue(eventStr.contains("to:"));
        });
    }

    @Test
    void constructor_invalidFormat_throwsException() {
        FionaException ex = Assertions.assertThrows(FionaException.class, () -> {
            new Event("Team meeting", "2027-02-20T1400", "2027-02-20 1500");
        });
        Assertions.assertTrue(ex.getMessage().contains("Invalid date-time format for event"),
                "Expected an error regarding the date-time format");
    }

    @Test
    void constructor_startAfterEnd_throwsException() {
        FionaException ex = Assertions.assertThrows(FionaException.class, () -> {
            new Event("Team meeting", "2027-02-20 1600", "2027-02-20 1500");
        });
        Assertions.assertTrue(ex.getMessage().contains("Start date time cannot be after end date time"),
                "Expected an error message indicating that the event's start time is after its end time.");
    }
}
