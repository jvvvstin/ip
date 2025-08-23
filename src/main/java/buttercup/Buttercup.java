package buttercup;

import buttercup.storage.Storage;

import buttercup.ui.Ui;

public class Buttercup {

    private static final String TASKS_FILEPATH = "data/";
    private static final String TASKS_FILENAME = "tasks.txt";

    public static void main(String[] args) {
        Storage storage = Storage.of(TASKS_FILEPATH, TASKS_FILENAME);
        Ui ui = new Ui(storage);
        ui.start();
    }
}
