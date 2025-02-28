package fiona.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fiona.command.Fiona;
import fiona.command.FionaException;
import fiona.command.Storage;
import fiona.command.TaskList;
import fiona.command.Ui;

public class FionaTest {
    private Fiona fiona;
    private TaskList testTaskList;

    static class DummyStorage extends Storage {
        private ArrayList<Task> tasks = new ArrayList<>();

        public DummyStorage() {
            super("dummyPath");
        }

        @Override
        public ArrayList<Task> load() {
            return tasks;
        }

        @Override
        public void save(List<Task> tasks) {
            this.tasks = new ArrayList<>(tasks);
        }
    }

    @BeforeEach
    void setUp() {
        Storage dummyStorage = new DummyStorage();
        testTaskList = new TaskList();
        Ui ui = new Ui();

        fiona = new Fiona(dummyStorage, testTaskList, ui);
    }

    @Test
    void addDeadline_validArgs_success() throws IOException, FionaException {
        String input = "deadline submit assignment /by 2035-01-31 2359";

        fiona.processCommand(input);
        Assertions.assertEquals(1, testTaskList.size());

        Task addedTask = testTaskList.getTasks().get(0);
        Assertions.assertInstanceOf(Deadline.class, addedTask, "The added task should be a Deadline.");
        Assertions.assertTrue(addedTask.toString().contains("submit assignment"));
    }

    @Test
    void addDeadline_missingByClause_throwsException() {
        String input = "deadline submit assignment";

        FionaException ex = Assertions.assertThrows(FionaException.class, () -> fiona.processCommand(input));

        Assertions.assertTrue(ex.getMessage().contains("/by"),
                "Error message should mention missing /by clause.");
    }

    @Test
    void addDeadline_invalidDateFormat_throwsException() {
        // Should be "yyyy-MM-dd HHmm"
        String input = "deadline submit assignment /by 2035-01-31T2359";

        FionaException ex = Assertions.assertThrows(FionaException.class, () -> fiona.processCommand(input));

        Assertions.assertTrue(ex.getMessage().contains("Invalid date-time format for deadline."),
                "Expected invalid date-time format error message.");
    }
}
