package fiona.task;
public abstract class Task {
    private int id;
    private String name;
    private boolean isDone;
    private static int numOfTask = 0;

    public Task(String name) {
        ++numOfTask;
        this.id = numOfTask;
        this.name = name;
        this.isDone = false;
    }

    public int getId() { return this.id; }

    public String getName() { return this.name; }

    public boolean getIsDone() { return this.isDone; }

    public void setDone() { this.isDone = true; }

    public void setUndone() { this.isDone = false; }
}