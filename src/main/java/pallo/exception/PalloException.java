package pallo.exception;

/**
 * Custom exception class for Pallo application errors.
 * Used to signal domain-specific errors such as invalid commands,
 * missing arguments, or invalid task numbers.
 */
public class PalloException extends Exception {
    /**
     * Constructs a new PalloException with the specified error message.
     *
     * @param message The error message describing what went wrong.
     */
    public PalloException(String message) {
        super(message);
    }
}
