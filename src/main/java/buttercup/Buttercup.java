package buttercup;

import buttercup.commands.Command;
import buttercup.exceptions.ButtercupException;
import buttercup.parsers.CommandParser;
import buttercup.storage.Storage;
import buttercup.ui.Main;
import buttercup.ui.Ui;
import javafx.application.Application;

/**
 * Represents the Buttercup chatbot.
 */
public class Buttercup {

    private static final String TASKS_FILEPATH = "data/";
    private static final String TASKS_FILENAME = "tasks.txt";
    private CommandParser commandParser;

    /**
     * Constructor for Buttercup class.
     * @param path File path directory for where the save file is located.
     * @param fileName File name of the save file.
     */
    public Buttercup(String path, String fileName) {
        Storage storage = Storage.of(path, fileName);
        this.commandParser = new CommandParser(storage);
    }

    /**
     * Gets response based on the user's input.
     * @param input Input entered in by a user.
     * @return A String representation of the response after processing the user's input.
     */
    public String getResponse(String input) {
        Command command = null;
        try {
            command = Command.getCommand(input.split(" ")[0]);
            return this.commandParser.processCommand(command, input);
        } catch (ButtercupException e) {
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        startGui();
//        startConsole();
    }

    /**
     * Starts the Buttercup chatbot JavaFX application.
     */
    public static void startGui() {
        Application.launch(Main.class);
    }

    /**
     * Starts the Buttercup chatbot on console.
     */
    public static void startConsole() {
        Storage storage = Storage.of(TASKS_FILEPATH, TASKS_FILENAME);
        Ui ui = new Ui(storage);
        ui.start();
    }
}
