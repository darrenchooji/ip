package fiona.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fiona.command.FionaException;

public class DeadlineTest {
    @Test
    void constructor_validFormat_success() {
        Assertions.assertDoesNotThrow(() -> {
            Deadline deadline = new Deadline("Finish CS2103T iP", "2035-01-31 1600");
            Assertions.assertEquals("[D][ ] Finish CS2103T iP (by: Jan 31 2035 16:00)",
                    deadline.toString(),
                    "Expected a correct format for toString() method"
            );
        });
    }

    @Test
    void constructor_invalidFormat_throwsException() {
        FionaException ex = Assertions.assertThrows(FionaException.class, () -> {
            new Deadline("Finish iP", "2030-31-01 1600");
        });
        Assertions.assertTrue(ex.getMessage().contains("Invalid date-time format for deadline."),
                "Should throw invalid date-time format error.");
    }


    @Test
    void getDeadline_validFormat_correctParsing() throws FionaException {
        Deadline deadline = new Deadline("Finish CS2103T iP", "2035-01-31 1600");
        Assertions.assertEquals(2035, deadline.getDeadline().getYear());
        Assertions.assertEquals(1, deadline.getDeadline().getMonthValue());
        Assertions.assertEquals(31, deadline.getDeadline().getDayOfMonth());
        Assertions.assertEquals(16, deadline.getDeadline().getHour());
        Assertions.assertEquals(0, deadline.getDeadline().getMinute());
    }
}
