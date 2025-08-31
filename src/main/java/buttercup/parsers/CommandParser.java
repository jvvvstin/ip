package buttercup.parsers;

import buttercup.commands.Command;
import buttercup.exceptions.ButtercupException;
import buttercup.storage.Storage;
import buttercup.tasks.Deadline;
import buttercup.tasks.Event;
import buttercup.tasks.Task;
import buttercup.tasks.TaskList;
import buttercup.tasks.Todo;
import buttercup.utils.DateTimeFormatUtils;

/**
 * Deals with the logic and making sense of the user's command.
 */
public class CommandParser {

    private final Storage storage;
    private static final String MARK_KEYWORD = "mark ";
    private static final String UNMARK_KEYWORD = "unmark ";
    private static final String DELETE_KEYWORD = "delete ";
    private static final String FIND_KEYWORD = "find ";
    private static final String TODO_KEYWORD = "task ";
    private static final String DEADLINE_KEYWORD = "deadline ";
    private static final String EVENT_KEYWORD = "event ";
    private static final int TODO_SUBSTRING_INDEX = TODO_KEYWORD.length();
    private static final int DEADLINE_SUBSTRING_INDEX = DEADLINE_KEYWORD.length();
    private static final int EVENT_SUBSTRING_INDEX = EVENT_KEYWORD.length();
    private static final int FIND_SUBSTRING_INDEX = FIND_KEYWORD.length();
    private static final int MARK_SUBSTRING_INDEX = MARK_KEYWORD.length();
    private static final int DELETE_SUBSTRING_INDEX = DELETE_KEYWORD.length();
    private static final int UNMARK_SUBSTRING_INDEX = UNMARK_KEYWORD.length();
    private static final String BY_FLAG = "/by";
    private static final String FROM_FLAG = "/from";
    private static final String TO_FLAG = "/to";
    private static final int INVALID_TASK_NUMBER = 0;

    public CommandParser(Storage storage) {
        this.storage = storage;
    }

    /**
     * Returns the result to be displayed to the user, based on the
     * command and input provided by the user.
     * @param command A type of command that the user wish to run
     * @param input Input provided by the user
     * @return A <code>String</code> result to be displayed to the
     *     user based on their command
     * @see Command
     */
    public String processCommand(Command command, String input) {
        String result = "";
        switch (command) {
        case LIST:
            result = displayTasks();
            break;
        case MARK:
            try {
                result = handleMarkTask(input);
            } catch (NumberFormatException e) {
                String errorMessage = "Invalid task number! Please enter in a valid task number e.g. mark 7.";
                System.out.println(errorMessage);
                return errorMessage;
            } catch (ButtercupException e) {
                System.out.println(e);
                return e.toString();
            }
            break;
        case UNMARK:
            try {
                result = handleUnmarkTask(input);
            } catch (NumberFormatException e) {
                String errorMessage = "Invalid task number! Please enter in a valid task number e.g. unmark 7.";
                System.out.println(errorMessage);
                return errorMessage;
            } catch (ButtercupException e) {
                System.out.println(e);
                return e.toString();
            }
            break;
        case TODO:
        case DEADLINE:
        case EVENT:
            try {
                result = addTask(input);
            } catch (ButtercupException e) {
                System.out.println(e);
                return e.toString();
            }
            break;
        case DELETE:
            try {
                result = handleDeleteTask(input);
            } catch (NumberFormatException e) {
                String errorMessage = "Invalid task number! Please enter in a valid task number e.g. delete 7.";
                System.out.println(errorMessage);
                return errorMessage;
            } catch (ButtercupException e) {
                System.out.println(e);
                return e.toString();
            }
            break;
        case FIND:
            try {
                result = handleFindTask(input);
            } catch (ButtercupException e) {
                System.out.println(e);
                return e.toString();
            }
            break;
        case BYE:
            return "Bye. Hope to see you again soon!";
        default:
            return result;
        }
        return result;
    }

    private String handleMarkTask(String input) throws ButtercupException, NumberFormatException {
        if (!input.startsWith(MARK_KEYWORD)) {
            throw new ButtercupException("Invalid mark command, try mark {taskNumber} instead.");
        }
        int taskNumber = Integer.parseInt(input.substring(MARK_SUBSTRING_INDEX).trim());
        return mark(taskNumber);
    }

    private String handleUnmarkTask(String input) throws ButtercupException, NumberFormatException {
        if (!input.startsWith(UNMARK_KEYWORD)) {
            throw new ButtercupException("Invalid unmark command, try unmark {taskNumber} instead.");
        }
        int taskNumber = Integer.parseInt(input.substring(UNMARK_SUBSTRING_INDEX).trim());
        return unmark(taskNumber);
    }

    private String handleDeleteTask(String input) throws ButtercupException, NumberFormatException {
        if (!input.startsWith(DELETE_KEYWORD)) {
            throw new ButtercupException("Invalid delete command, try delete {taskNumber} instead.");
        }
        int taskNumber = Integer.parseInt(input.substring(DELETE_SUBSTRING_INDEX).trim());
        return deleteTask(taskNumber);
    }

    private String handleFindTask(String input) throws ButtercupException {
        if (!input.startsWith(FIND_KEYWORD)) {
            throw new ButtercupException("Invalid find command, try find {keyword} instead.");
        }
        String keyword = input.substring(FIND_SUBSTRING_INDEX).trim();
        return findTask(keyword);
    }

    /**
     * Returns a <code>String</code> representation of all the
     * current tasks to be displayed on the UI.
     * @return A <code>String</code> representation of all the
     *     current tasks to be displayed on the UI.
     */
    public String displayTasks() {
        if (storage.getTasks().isEmpty()) {
            return "There are no tasks in the list.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        sb.append(this.storage.getTasks().toString());
        return sb.toString();
    }

    /**
     * Marks the task of the specified number as completed.
     * @param taskNumber The index of the task to be mark as completed.
     * @return A <code>String</code> of the outcome of marking the task
     *     at the specified index as complete.
     * @throws ButtercupException If taskNumber is not a valid number
     *     (i.e. taskNumber <= 0 etc.)
     */
    public String mark(int taskNumber) throws ButtercupException {
        if (storage.getTasks().isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        boolean isInvalidIndex = taskNumber <= INVALID_TASK_NUMBER || taskNumber > this.storage.getTasks().getSize();
        if (isInvalidIndex) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - "
                    + this.storage.getTasks().getSize() + " e.g. mark 7.");
        }
        Task task = this.storage.getTasks().getTask(taskNumber - 1);
        storage.setTaskCompletion(task, true);
        return "Nice! I've marked this task as done:\n" + task.toString();
    }

    /**
     * Marks the task of the specified number as not completed.
     * @param taskNumber The index of the task to be mark as not completed.
     * @return A <code>String</code> of the outcome of marking the task
     *     at the specified index as incomplete.
     * @throws ButtercupException If taskNumber is not a valid number
     *     (i.e. taskNumber <= 0 etc.)
     */
    public String unmark(int taskNumber) throws ButtercupException {
        if (storage.getTasks().isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        boolean isInvalidIndex = taskNumber <= INVALID_TASK_NUMBER || taskNumber > this.storage.getTasks().getSize();
        if (isInvalidIndex) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - "
                    + this.storage.getTasks().getSize() + " e.g. unmark 7.");
        }
        Task task = this.storage.getTasks().getTask(taskNumber - 1);
        storage.setTaskCompletion(task, false);
        return "OK, I've marked this task as not done yet:\n" + task.toString();
    }

    /**
     * Creates and adds a new task to the current list of tasks.
     * @param input The description of the task to be added.
     * @return A <code>String</code> of the outcome of creating and
     *     adding the new task.
     * @throws ButtercupException If the input is invalid or of invalid
     *     format
     */
    public String addTask(String input) throws ButtercupException {
        Task newTask;
        if (input.startsWith(TODO_KEYWORD)) {
            newTask = handleAddTodo(input);
        } else if (input.startsWith(DEADLINE_KEYWORD)) {
            newTask = handleAddDeadline(input);
        } else if (input.startsWith(EVENT_KEYWORD)) {
            newTask = handleAddEvent(input);
        } else {
            handleInvalidTasks(input);
            return "";
        }

        this.storage.addTask(newTask);

        String str = String.format("Got it. I've added this task:\n"
                        + "%s\n"
                        + "Now you have %d %s in the list.",
                newTask, this.storage.getTasks().getSize(), this.storage.getTasks().getSize() == 1 ? "task" : "tasks");
        return str;
    }

    private Task handleAddTodo(String input) {
        return new Todo(input.substring(TODO_SUBSTRING_INDEX).trim());
    }

    private Task handleAddDeadline(String input) throws ButtercupException {
        input = input.substring(DEADLINE_SUBSTRING_INDEX);
        if (!input.contains(BY_FLAG)) {
            throw new ButtercupException("Invalid format, deadline command should contain '/by'"
                    + " and be of the format deadline {description} /by {deadline} instead.");
        }
        String[] splitted = input.split(BY_FLAG);
        if (splitted.length != 2) {
            throw new ButtercupException("Invalid format, deadline command should be of the format deadline "
                    + "{description} /by {deadline} instead.");
        }
        if (splitted[0].trim().isEmpty()) {
            throw new ButtercupException("Invalid format, deadline's description should not be empty and should "
                    + "be of the format deadline {description} /by {deadline} instead.");
        }
        if (splitted[1].trim().isEmpty()) {
            throw new ButtercupException("Invalid format, deadline's deadline should not be empty and should "
                    + "be of the format deadline {description} /by {deadline} instead.");
        }

        return new Deadline(splitted[0].trim(),
                DateTimeFormatUtils.getLocalDateTimeFromString(splitted[1].trim()));
    }

    private Task handleAddEvent(String input) throws ButtercupException {
        input = input.substring(EVENT_SUBSTRING_INDEX).trim();
        if (!input.contains(FROM_FLAG)) {
            throw new ButtercupException("Invalid format, event command should contain '/from' and be of the format"
                    + " event {description} /from {start} /to {end} instead.");
        }
        if (!input.contains(TO_FLAG)) {
            throw new ButtercupException("Invalid format, event command should contain '/to' and be of the format "
                    + "event {description} /from {start} /to {end} instead.");
        }
        String[] splitted = input.split(FROM_FLAG);
        if (splitted.length != 2 || splitted[1].trim().isEmpty()) {
            throw new ButtercupException("Invalid format, event command should be of the format event {description}"
                    + " /from {start} /to {end} instead.");
        }
        String description = splitted[0].trim();
        if (description.isEmpty()) {
            throw new ButtercupException("Invalid format, event's description should not be empty and should be of"
                    + " the format event {description} /from {start} /to {end} instead.");
        }
        splitted = splitted[1].split(TO_FLAG);
        if (splitted.length != 2) {
            throw new ButtercupException("Invalid format, event command should be of the format event {description}"
                    + " /from {start} /to {end} instead.");
        }
        String from = splitted[0].trim();
        String to = splitted[1].trim();
        if (from.isEmpty()) {
            throw new ButtercupException("Invalid format, event's start should not be empty and should be of the"
                    + " format event {description} /from {start} /to {end} instead.");
        }
        if (to.isEmpty()) {
            throw new ButtercupException("Invalid format, event's end should not be empty and should be of the"
                    + " format event {description} /from {start} /to {end} instead.");
        }
        return new Event(description, DateTimeFormatUtils.getLocalDateTimeFromString(from),
                DateTimeFormatUtils.getLocalDateTimeFromString(to));
    }

    /**
     * Removes the task of the specified number from the list
     * of current tasks.
     * @param taskNumber The index of the task to be removed.
     * @return A <code>String</code> of the outcome of removing the task
     *     at the specified index from the list of current tasks.
     * @throws ButtercupException If taskNumber is not a valid number
     *     (i.e. taskNumber <= 0)
     */
    private String deleteTask(int taskNumber) throws ButtercupException {
        if (this.storage.getTasks().isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        boolean isInvalidIndex = taskNumber <= INVALID_TASK_NUMBER || taskNumber > this.storage.getTasks().getSize();
        if (isInvalidIndex) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - "
                    + this.storage.getTasks().getSize() + " e.g. delete 7.");
        }
        Task task = this.storage.deleteTask(taskNumber);
        return "Noted! I've removed this task:\n" + task.toString();
    }

    /**
     * Handles invalid Todo, Deadline and Event formats and throws
     * an exception displaying the right formats for the respective
     * tasks.
     * @param input The input entered in by the user.
     * @throws ButtercupException If the input entered in by the user
     *     is invalid
     */
    private void handleInvalidTasks(String input) throws ButtercupException {
        if (input.equals("todo")) {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try"
                    + " todo {description} instead.");
        } else if (input.equals("deadline")) {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try"
                    + " deadline {description} /by {deadline} instead.");
        } else {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try"
                    + " event {description} /from {start} /to {end} instead.");
        }
    }

    private String findTask(String keyword) throws ButtercupException {
        if (keyword.isEmpty()) {
            throw new ButtercupException("No keyword provided! Try find {keyword} instead.");
        }
        TaskList filteredTasks = new TaskList(storage.getTasks().filterByKeyword(keyword));
        if (filteredTasks.isEmpty()) {
            return "There are no tasks matching the keyword: " + keyword;
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        sb.append(filteredTasks);
        return sb.toString();
    }
}
