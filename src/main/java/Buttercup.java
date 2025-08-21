import exceptions.ButtercupException;
import utils.DateTimeFormatUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Buttercup {

    private static List<Task> tasks = new ArrayList<>();
    private static final String TASKS_FILEPATH = "data";
    private static final String TASKS_FILENAME = TASKS_FILEPATH + "/tasks.txt";

    public static void main(String[] args) {
        Storage storage = Storage.of(TASKS_FILEPATH, TASKS_FILENAME);
        Ui ui = new Ui(storage);
        ui.start();
    }
}
