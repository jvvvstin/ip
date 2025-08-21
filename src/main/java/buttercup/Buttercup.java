package buttercup;

import buttercup.storage.Storage;
import buttercup.tasks.Task;
import buttercup.ui.Ui;

import java.util.List;
import java.util.ArrayList;

public class Buttercup {

    private static final String TASKS_FILEPATH = "data";
    private static final String TASKS_FILENAME = TASKS_FILEPATH + "/tasks.txt";

    public static void main(String[] args) {
        Storage storage = Storage.of(TASKS_FILEPATH, TASKS_FILENAME);
        Ui ui = new Ui(storage);
        ui.start();
    }
}
