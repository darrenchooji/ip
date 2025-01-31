package fiona.command;

/**
 * The {@code Action} enum represents the different types of commands
 * that can be executed in the Fiona chatbot.
 */
public enum Action {
    /** Represents the action to add a "todo" task. */
    TODO,

    /** Represents the action to add a "deadline" task. */
    DEADLINE,

    /** Represents the action to add an "event" task. */
    EVENT,

    /** Represents the action to list all tasks. */
    LIST,

    /** Represents the action to mark a task as completed. */
    MARK,

    /** Represents the action to unmark a task (mark as not done). */
    UNMARK,

    /** Represents the action to delete a task. */
    DELETE,

    /** Represents the action to exit the program. */
    BYE,

    /** Represents the action to find tasks based on a date. */
    FIND,

    /** Represents the action to find tasks based on keywords. */
    FIND_KEYWORD,

    /** Represents an unknown or invalid action. */
    UNKNOWN;

    /**
     * Converts a string into an {@code Action} enum value.
     * If the provided string does not match any known action, {@code UNKNOWN} is returned.
     *
     * @param action The string representation of an action.
     * @return The corresponding {@code Action} enum value, or {@code UNKNOWN} if invalid.
     */
    public static Action fromString(String action) {
        try {
            return Action.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

