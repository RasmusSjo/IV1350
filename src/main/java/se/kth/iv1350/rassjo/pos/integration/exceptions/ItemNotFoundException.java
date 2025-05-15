package se.kth.iv1350.rassjo.pos.integration.exceptions;

/**
 * This exception is thrown to indicate that an item with the
 * provided id doesn't exist in the inventory system.
 */
public class ItemNotFoundException extends Exception {

    /**
     * Creates a new instance of {@code ItemNotFoundException} with a specified detail message.
     *
     * @param message the detail message.
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}
