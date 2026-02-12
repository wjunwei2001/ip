package pallo.storage;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import pallo.exception.PalloException;
import pallo.task.Deadline;
import pallo.task.Event;
import pallo.task.Task;
import pallo.task.TaskStatus;
import pallo.task.Todo;

/**
 * Handles loading and saving of tasks to a file.
 * Tasks are persisted in a pipe-delimited text format that includes
 * task type, status, description, and any date/time fields.
 */
public class Storage {
    private final Path filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath The path to the file where tasks will be stored.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads tasks from the storage file.
     * Creates the directory if it doesn't exist. Returns an empty list if
     * the file doesn't exist. Skips corrupted lines gracefully.
     *
     * @return An ArrayList of tasks loaded from the file.
     * @throws PalloException If there is an error reading the file.
     */
    public ArrayList<Task> load() throws PalloException {
        ArrayList<Task> tasks = new ArrayList<>();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            throw new PalloException("OH NO!!! Failed to create data directory: " + e.getMessage());
        }

        // If file doesn't exist, return empty list
        if (!Files.exists(filePath)) {
            return tasks;
        }

        // Read and parse file
        try (Scanner scanner = new Scanner(filePath)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    Task task = parseTaskFromFile(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    // Handle corrupted lines gracefully - skip them and continue
                    System.err.println("Warning: Skipping corrupted line " + lineNumber + ": " + line);
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist, return empty list
            return tasks;
        } catch (IOException e) {
            throw new PalloException("OH NO!!! Error reading file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves the given tasks to the storage file.
     * Creates the directory if it doesn't exist.
     *
     * @param tasks The list of tasks to save.
     * @throws PalloException If there is an error writing to the file.
     */
    public void save(ArrayList<Task> tasks) throws PalloException {
        // Create directory if it doesn't exist
        try {
            Files.createDirectories(filePath.getParent());
        } catch (IOException e) {
            throw new PalloException("OH NO!!! Failed to create data directory: " + e.getMessage());
        }

        // Write tasks to file
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new PalloException("OH NO!!! Error saving file: " + e.getMessage());
        }
    }

    private Task parseTaskFromFile(String line) throws PalloException {
        String[] parts = line.split(" \\| ", -1); // -1 to keep empty strings

        if (parts.length < 3) {
            throw new PalloException("Invalid file format: insufficient parts");
        }

        assert parts.length >= 3 : "File line should have at least 3 pipe-delimited parts";
        String taskType = parts[0].trim();
        String statusStr = parts[1].trim();
        String description = parts[2].trim();

        // Parse status (1 = done, 0 = not done)
        TaskStatus status = statusStr.equals("1") ? TaskStatus.DONE : TaskStatus.NOT_DONE;

        Task task;
        switch (taskType) {
        case "T":
            if (parts.length != 3) {
                throw new PalloException("Invalid Todo format");
            }
            task = new Todo(description);
            break;
        case "D":
            if (parts.length != 4) {
                throw new PalloException("Invalid Deadline format");
            }
            String by = parts[3].trim();
            // Try to parse as LocalDateTime first, fall back to string
            LocalDateTime byDateTime = DateParser.parseDateTimeFromFile(by);
            if (byDateTime != null) {
                task = new Deadline(description, byDateTime);
            } else {
                task = new Deadline(description, by);
            }
            break;
        case "E":
            if (parts.length != 5) {
                throw new PalloException("Invalid Event format");
            }
            String from = parts[3].trim();
            String to = parts[4].trim();
            // Try to parse as LocalDateTime first, fall back to string
            LocalDateTime fromDateTime = DateParser.parseDateTimeFromFile(from);
            LocalDateTime toDateTime = DateParser.parseDateTimeFromFile(to);
            if (fromDateTime != null && toDateTime != null) {
                task = new Event(description, fromDateTime, toDateTime);
            } else {
                task = new Event(description, from, to);
            }
            break;
        default:
            throw new PalloException("Unknown task type: " + taskType);
        }

        assert task != null : "Parsed task should not be null";
        // Set the status
        if (status == TaskStatus.DONE) {
            task.markAsDone();
        }

        return task;
    }
}
