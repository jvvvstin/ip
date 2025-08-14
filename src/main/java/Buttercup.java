public class Buttercup {
    public static void main(String[] args) {
        displayLine();
        greet();
        displayLine();
        exit();
        displayLine();
    }

    public static void greet() {
        String message = "Hello! I'm Buttercup\n" +
                         "What can I do for you?";
        System.out.println(message);
    }

    public static void exit() {
        String exitMessage = "Bye. Hope to see you again soon!";
        System.out.println(exitMessage);
    }

    public static void displayLine() {
        System.out.println("____________________________________________________________");
    }
}
