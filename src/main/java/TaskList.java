import java.util.List;
import java.util.ArrayList;

public class TaskList {
    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> loadedTasks) {
        this.tasks = loadedTasks;
    }

    public void add(Task t) {
        this.tasks.add(t);
    }

    public Task mark(int index) throws FionaException {
        if (index < 0 || index >= this.size()) {
            throw new FionaException("You must specify a valid task number to mark as done.");
        }
        Task task = tasks.get(index);
        task.setDone();
        return task;
    }

    public Task unmark(int index) throws FionaException {
        if (index < 0 || index >= this.size()) {
            throw new FionaException("You must specify a valid task number to mark as not done yet.");
        }
        Task task = tasks.get(index);
        task.setUndone();
        return task;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public int size() {
        return this.tasks.size();
    }
}
