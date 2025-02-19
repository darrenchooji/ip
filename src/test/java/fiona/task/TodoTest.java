package fiona.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fiona.command.FionaException;

public class TodoTest {

    @Test
    void constructor_validDescription_success() {
        Assertions.assertDoesNotThrow(() -> {
            Todo todo = new Todo("Study for exam");
            Assertions.assertEquals("[T][ ] Study for exam",
                    todo.toString(),
                    "Expected toString() to display an uncompleted Todo correctly");
        });
    }

    @Test
    void markTodo_done_success() throws FionaException {
        Todo todo = new Todo("Buy groceries");
        todo.setDone();
        Assertions.assertEquals("[T][X] Buy groceries",
                todo.toString(),
                "Expected toString() to display a completed Todo correctly");
    }
}
