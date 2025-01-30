public class Command {
    private Action action;
    private String args;

    public Command(Action action, String args) {
        this.action = action;
        this.args = args;
    }

    public Action getAction() {
        return action;
    }

    public String getArgs() {
        return args;
    }
}

