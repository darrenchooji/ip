package fiona.task;
public class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[T][" + doneIndicator + "] " + super.getName();
    }
}
