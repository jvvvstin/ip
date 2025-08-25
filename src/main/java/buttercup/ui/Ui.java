package buttercup.ui;

import java.util.Scanner;

import buttercup.commands.Command;
import buttercup.exceptions.ButtercupException;
import buttercup.parsers.CommandParser;
import buttercup.storage.Storage;

/**
 * Represents the UI of Buttercup chatbot and deals with interactions
 * with the user, such as displaying output.
 */
public class Ui {
    private CommandParser parser;

    /**
     * Constructor for Ui class
     * @param storage A storage object for the UI
     * @see Storage
     */
    public Ui(Storage storage) {
        this.parser = new CommandParser(storage);
    }

    /**
     * Displays a separator line to the user.
     */
    public void displayLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a greeting message to the user
     */
    public void greet() {
        displayLine();
        String message = "Hello! I'm Buttercup\n"
                + "What can I do for you?";
        System.out.println(message);
        displayLine();
    }

    /**
     * Starts the Buttercup chatbot program.
     */
    public void start() {
        greet();
        beginPrompt();
    }

    /**
     * Prompts the user for what they would like to do. The program
     * ends when the user enters the input 'bye'.
     */
    public void beginPrompt() {
        String input = "";
        Scanner scanner = new Scanner(System.in);

        while (!(input.equals("bye"))) {
            // Read user input
            input = scanner.nextLine().trim();
            displayLine();
            Command command = null;
            try {
                command = Command.getCommand(input.split(" ")[0]);
            } catch (ButtercupException e) {
                System.out.println(e);
                displayLine();
                continue;
            }
            switch (command) {
            case BYE:
                exit();
                break;
            default:
                String result = this.parser.processCommand(command, input);
                if (!result.equals("")) {
                    System.out.println(result);
                }
            }
            displayLine();
        }
    }

    /**
     * Displays the exit message to the user.
     */
    public void exit() {
        String exitMessage = "Bye. Hope to see you again soon!";
        System.out.println(exitMessage);
    }


}
