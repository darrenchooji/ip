public class Task {
    private int id;
    private String name;
    private boolean isDone;
    private static int ID = 0;

    public Task(String name) {
        this.name = name;
        this.isDone = false;
        ID++;
        this.id = ID;
    }

    public void setIsDone() {
        this.isDone = true;
    }

    public void setNotDone() {
        this.isDone = false;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public String getTaskName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
