import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "pallo.txt";
    private final Path filePath;

    public Storage() {
        this.filePath = Paths.get(DATA_DIR, DATA_FILE);
    }

    public ArrayList<Task> loadTasks() throws PalloException {
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

    public void saveTasks(ArrayList<Task> tasks) throws PalloException {
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
            task = new Deadline(description, by);
            break;
        case "E":
            if (parts.length != 5) {
                throw new PalloException("Invalid Event format");
            }
            String from = parts[3].trim();
            String to = parts[4].trim();
            task = new Event(description, from, to);
            break;
        default:
            throw new PalloException("Unknown task type: " + taskType);
        }

        // Set the status
        if (status == TaskStatus.DONE) {
            task.markAsDone();
        }

        return task;
    }
}
