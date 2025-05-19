package se.kth.iv1350.rassjo.pos.application.exceptions;

/**
 * Represents an unchecked exception that is thrown when an operation in the system fails.
 * This exception is a runtime variation of the {@link OperationFailedException}, allowing
 * it to propagate unchecked without requiring explicit handling.
 */
public class UncheckedOperationFailedException extends RuntimeException {

    /**
     * Constructs a new {@link UncheckedOperationFailedException} with the specified detail message.
     *
     * @param message the detail message that provides information about the cause of the exception.
     */
    public UncheckedOperationFailedException(String message) {
        super(message);
    }

    /**
     * Creates an instance of the {@link UncheckedOperationFailedException} class with a specific detail message.
     *
     * @param message the detail message that explains the reason for the exception.
     * @param cause the {@code Exception} that caused the exception.
     */
    public UncheckedOperationFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
