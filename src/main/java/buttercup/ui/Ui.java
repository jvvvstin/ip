package buttercup.ui;

import buttercup.commands.Command;
import buttercup.parsers.CommandParser;
import buttercup.storage.Storage;
import buttercup.exceptions.ButtercupException;
import java.util.Scanner;

public class Ui {

    private Scanner scanner;
    private CommandParser parser;

    public Ui(Storage storage) {
        this.scanner = new Scanner(System.in);
        this.parser = new CommandParser(storage);
    }

    public void displayLine() {
        System.out.println("____________________________________________________________");
    }

    public void greet() {
        displayLine();
        String message = "Hello! I'm Buttercup\n" +
                "What can I do for you?";
        System.out.println(message);
        displayLine();
    }

    public void start() {
        greet();
        beginPrompt();
    }

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

    public void exit() {
        String exitMessage = "Bye. Hope to see you again soon!";
        System.out.println(exitMessage);
    }


}
