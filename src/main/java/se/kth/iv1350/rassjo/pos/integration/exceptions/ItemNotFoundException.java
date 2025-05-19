package se.kth.iv1350.rassjo.pos.integration.exceptions;

import se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;

/**
 * This exception is thrown to indicate that an item with the
 * provided id doesn't exist in the inventory system.
 */
public class ItemNotFoundException extends Exception {

    /**
     * Creates a new instance of {@code ItemNotFoundException} with
     * a detail message given the provided item id.
     *
     * @param unknownItemId the item id of the item that wasn't found.
     */
    public ItemNotFoundException(ItemIdentifierDTO unknownItemId) {
        super("Item with ID '" + unknownItemId.toString() + "' not found in inventory.");
    }
}
