package buttercup.commands;

import buttercup.exceptions.ButtercupException;

public enum Command {
    BYE("bye"),
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    FIND("find");

    private final String keyword;

    Command(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public static Command getCommand(String keyword) throws ButtercupException {
        for (Command command : Command.values()) {
            if (keyword.startsWith(command.getKeyword())) {
                return command;
            }
        }
        throw new ButtercupException("Invalid command '" + keyword + "'. Please try again.");
    }
}
