package fiona.task;

/**
 * The {@code Todo} class represents a task that does not have a specific deadline or time frame.
 * It extends the {@code Task} class and provides a string representation for display.
 */
public class Todo extends Task {

    /**
     * Constructs a {@code Todo} task with the specified name.
     *
     * @param name The name or description of the todo task.
     */
    public Todo(String name) {
        super(name);
    }

    /**
     * Returns a string representation of the todo task, including its status.
     *
     * @return A formatted string representing the todo task.
     */
    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[T][" + doneIndicator + "] " + super.getName();
    }
}
