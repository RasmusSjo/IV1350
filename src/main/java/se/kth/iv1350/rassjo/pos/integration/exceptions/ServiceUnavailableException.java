package se.kth.iv1350.rassjo.pos.integration.exceptions;

/**
 * Represents an exception thrown when an external system service is unavailable.
 */
public class ServiceUnavailableException extends Exception {

    /**
     * Creates an instance of {@code ServiceUnavailableException} with a specified detail message
     *
     * @param message the detail message.
     */
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
