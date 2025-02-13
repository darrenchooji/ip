package fiona.task;

/**
 * The {@code Task} class represents a general task with an ID, name, and completion status.
 * It serves as an abstract base class for specific task types such as {@code Todo},
 * {@code Deadline}, and {@code Event}.
 */
public abstract class Task {
    private static int numOfTask = 0;

    private int id;
    private String name;
    private boolean isDone;

    /**
     * Constructs a new {@code Task} with the specified name.
     * Each task is assigned a unique ID based on the number of tasks created.
     *
     * @param name The name or description of the task.
     */
    public Task(String name) {
        assert name != null : "Task name cannot be null";
        assert !name.isEmpty() : "Task name cannot be empty";
        ++numOfTask;
        this.id = numOfTask;
        this.name = name;
        this.isDone = false;
    }

    /**
     * Returns the unique ID of the task.
     *
     * @return The task ID.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name or description of the task.
     *
     * @return The task name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks whether the task is marked as done.
     *
     * @return {@code true} if the task is completed, {@code false} otherwise.
     */
    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Marks the task as done.
     */
    public void setDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void setUndone() {
        this.isDone = false;
    }
}
