public class Deadline extends Task {
    private String deadline;

    public Deadline(String name, String deadline) {
        super(name);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        String doneIndicator = super.getIsDone() ? "X" : " ";
        return "[D][" + doneIndicator + "] " + super.getName() 
                + " (by: " + this.deadline + ")";
    }
}
