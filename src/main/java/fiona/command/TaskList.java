package fiona.command;

import fiona.task.Deadline;
import fiona.task.Event;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fiona.task.Task;

/**
 * The {@code TaskList} class represents a list of tasks managed by the Fiona chatbot.
 * It provides methods for adding, retrieving, marking, and unmarking tasks.
 */
public class TaskList {
    /** The list of tasks managed by this {@code TaskList}. */
    private List<Task> tasks;

    /**
     * Constructs an empty {@code TaskList}.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a {@code TaskList} with a given list of tasks.
     *
     * @param loadedTasks The list of tasks to initialize the task list with.
     */
    public TaskList(List<Task> loadedTasks) {
        this.tasks = loadedTasks;
    }

    /**
     * Adds a new task to the task list.
     *
     * @param t The task to be added.
     */
    public void add(Task t) {
        this.tasks.add(t);
    }

    /**
     * Marks a task as done based on its index.
     *
     * @param index The index of the task to mark as done (0-based index).
     * @return The task that was marked as done.
     * @throws FionaException If the specified index is out of bounds.
     */
    public Task mark(int index) throws FionaException {
        if (index < 0 || index >= this.size()) {
            throw new FionaException("You must specify a valid task number to mark as done.");
        }
        Task task = tasks.get(index);
        task.setDone();
        return task;
    }

    /**
     * Marks a task as not done based on its index.
     *
     * @param index The index of the task to mark as not done (0-based index).
     * @return The task that was marked as not done.
     * @throws FionaException If the specified index is out of bounds.
     */
    public Task unmark(int index) throws FionaException {
        if (index < 0 || index >= this.size()) {
            throw new FionaException("You must specify a valid task number to mark as not done yet.");
        }
        Task task = tasks.get(index);
        task.setUndone();
        return task;
    }

    /**
     * Delete past Deadlines or Events.
     */
    public void purgeOverdueTasks() {
        tasks.removeIf(task -> {
            if (task instanceof Deadline) {
                return ((Deadline) task).getDeadline().isBefore(LocalDateTime.now());
            } else if (task instanceof Event) {
                return ((Event) task).getTo().isBefore(LocalDateTime.now());
            }
            return false;
        });
    }


    /**
     * Returns the list of tasks.
     *
     * @return A {@code List} containing all tasks.
     */
    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Returns the number of tasks in the task list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return this.tasks.size();
    }
}
