package se.kth.iv1350.rassjo.pos.application.exceptions;

/**
 * Represents an exception thrown when there is an issue related to performing a
 * certain operation in the sale. Exception contains a descriptive message of the failure.
 */
public class OperationFailedException extends Exception {

    /**
     * Constructs a new {@link OperationFailedException} with the specified detail message.
     *
     * @param message the detail message that provides information about the cause of the exception.
     */
    public OperationFailedException(String message) {
        super(message);
    }

    /**
     * Creates an instance of the {@link OperationFailedException} class with a specific detail message.
     *
     * @param message the detail message that explains the reason for the exception.
     * @param cause the {@code Exception} that caused the exception.
     */
    public OperationFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
