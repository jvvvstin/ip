package buttercup.parsers;

import buttercup.commands.Command;

import buttercup.exceptions.ButtercupException;

import buttercup.storage.Storage;

import buttercup.tasks.Deadline;
import buttercup.tasks.Event;
import buttercup.tasks.Task;
import buttercup.tasks.Todo;

import buttercup.utils.DateTimeFormatUtils;

public class CommandParser {
    private final Storage storage;

    public CommandParser(Storage storage) {
        this.storage = storage;
    }

    public String processCommand(Command command, String input) {
        String result = "";
        switch (command) {
        case LIST:
            result = displayTasks();
            break;
        case MARK:
            try {
                int taskNumber = Integer.parseInt(input.substring(5).trim());
                result = mark(taskNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid task number! Please enter in a valid task number e.g. mark 7.");
            } catch (ButtercupException e) {
                System.out.println(e);
            }
            break;
        case UNMARK:
            try {
                int taskNumber = Integer.parseInt(input.substring(7).trim());
                result = unmark(taskNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid task number! Please enter in a valid task number e.g. unmark 7.");
            } catch (ButtercupException e) {
                System.out.println(e);
            }
            break;
        case TODO:
        case DEADLINE:
        case EVENT:
            try {
                result = addTask(input);
            } catch (ButtercupException e) {
                System.out.println(e);
            }
            break;
        case DELETE:
            try {
                int taskNumber = Integer.parseInt(input.substring(7).trim());
                result = deleteTask(taskNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid task number! Please enter in a valid task number e.g. delete 7.");
            } catch (ButtercupException e) {
                System.out.println(e);
            }
            break;
        default:
            return result;
        }
        return result;
    }

    public String displayTasks() {
        if (storage.getTasks().isEmpty()) {
            return "There are no tasks in the list.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        sb.append(this.storage.getTasks().toString());
        return sb.toString();
    }

    public String mark(int taskNumber) throws ButtercupException {
        if (storage.getTasks().isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        if (taskNumber <= 0 || taskNumber > this.storage.getTasks().getSize()) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - "
                    + this.storage.getTasks().getSize() + " e.g. mark 7.");
        }
        Task task = this.storage.getTasks().getTask(taskNumber - 1);
        storage.setTaskCompletion(task, true);
        return "Nice! I've marked this task as done:\n" + task.toString();
    }

    public String unmark(int taskNumber) throws ButtercupException {
        if (storage.getTasks().isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        if (taskNumber <= 0 || taskNumber > this.storage.getTasks().getSize()) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - "
                    + this.storage.getTasks().getSize() + " e.g. unmark 7.");
        }
        Task task = this.storage.getTasks().getTask(taskNumber - 1);
        storage.setTaskCompletion(task, false);
        return "OK, I've marked this task as not done yet:\n" + task.toString();
    }

    public String addTask(String input) throws ButtercupException {
        Task newTask = null;
        if (input.startsWith("todo ")) {
            newTask = new Todo(input.substring(5).trim());
        } else if (input.startsWith("deadline ")) {
            input = input.substring(9);
            if (!input.contains("/by")) {
                throw new ButtercupException("Invalid format, deadline command should contain '/by' and be of the format deadline {description} /by {deadline} instead.");
            }
            String[] splitted = input.split("/by");
            if (splitted.length != 2) {
                throw new ButtercupException("Invalid format, deadline command should be of the format deadline {description} /by {deadline} instead.");
            }
            if (splitted[0].trim().isEmpty()) {
                throw new ButtercupException("Invalid format, deadline's description should not be empty and should be of the format deadline {description} /by {deadline} instead.");
            }
            if (splitted[1].trim().isEmpty()) {
                throw new ButtercupException("Invalid format, deadline's deadline should not be empty and should be of the format deadline {description} /by {deadline} instead.");
            }

            newTask = new Deadline(splitted[0].trim(), DateTimeFormatUtils.getLocalDateTimeFromString(splitted[1].trim()));
        } else if (input.startsWith("event ")) {
            input = input.substring(6).trim();
            if (!input.contains("/from")) {
                throw new ButtercupException("Invalid format, event command should contain '/from' and be of the format event {description} /from {start} /to {end} instead.");
            }
            if (!input.contains("/to")) {
                throw new ButtercupException("Invalid format, event command should contain '/to' and be of the format event {description} /from {start} /to {end} instead.");
            }
            String[] splitted = input.split("/from");
            if (splitted.length != 2 || splitted[1].trim().isEmpty()) {
                throw new ButtercupException("Invalid format, event command should be of the format event {description} /from {start} /to {end} instead.");
            }
            String description = splitted[0].trim();
            if (description.isEmpty()) {
                throw new ButtercupException("Invalid format, event's description should not be empty and should be of the format event {description} /from {start} /to {end} instead.");
            }
            splitted = splitted[1].split("/to");
            if (splitted.length != 2) {
                throw new ButtercupException("Invalid format, event command should be of the format event {description} /from {start} /to {end} instead.");
            }
            String from = splitted[0].trim();
            String to = splitted[1].trim();
            if (from.isEmpty()) {
                throw new ButtercupException("Invalid format, event's start should not be empty and should be of the format event {description} /from {start} /to {end} instead.");
            }
            if (to.isEmpty()) {
                throw new ButtercupException("Invalid format, event's end should not be empty and should be of the format event {description} /from {start} /to {end} instead.");
            }
            newTask = new Event(description, DateTimeFormatUtils.getLocalDateTimeFromString(from), DateTimeFormatUtils.getLocalDateTimeFromString(to));
        } else {
            handleInvalidTasks(input);
            return "";
        }

        this.storage.addTask(newTask);

        String str = String.format("Got it. I've added this task:\n" +
                        "%s\n" +
                        "Now you have %d %s in the list.",
                newTask, this.storage.getTasks().getSize(), this.storage.getTasks().getSize() == 1 ? "task" : "tasks");
        return str;
    }

    private String deleteTask(int taskNumber) throws ButtercupException {
        if (this.storage.getTasks().isEmpty()) {
            throw new ButtercupException("There are no tasks in the list.");
        }
        if (taskNumber <= 0 || taskNumber > this.storage.getTasks().getSize()) {
            throw new ButtercupException("Invalid task number! Please enter in a valid task number from 1 - "
                    + this.storage.getTasks().getSize() + " e.g. delete 7.");
        }
        Task task = this.storage.deleteTask(taskNumber);
        return "Noted! I've removed this task:\n" + task.toString();
    }

    private void handleInvalidTasks(String input) throws ButtercupException {
        if (input.equals("todo")) {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try todo {description} instead.");
        } else if (input.equals("deadline")) {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try deadline {description} /by {deadline} instead.");
        } else {
            throw new ButtercupException("Invalid command, the description of a " + input + " cannot be left empty. Try event {description} /from {start} /to {end} instead.");
        }
    }
}
