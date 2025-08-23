package buttercup.commands;

import buttercup.exceptions.ButtercupException;

/**
 *  Represents a valid command based on the currently valid and existing
 *  commands.
 */
public enum Command {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete");

    private final String keyword;

    Command(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }

    /**
     * Returns the Command object based on the keyword provided.
     * @param keyword The keyword to be processed.
     * @return A Command object based on the keyword provided
     * @throws ButtercupException If there was an invalid command provided.
     */
    public static Command getCommand(String keyword) throws ButtercupException {
        for (Command command : Command.values()) {
            if (keyword.startsWith(command.getKeyword())) {
                return command;
            }
        }
        throw new ButtercupException("Invalid command '" + keyword + "'. Please try again.");
    }
}
