package se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents the unique identifier for an item in the inventory system.
 *
 * @param id the identifier of the item.
 */
public record ItemIdentifierDTO(int id) {

    /**
     * Returns the string representation of the identifier.
     *
     * @return a string representation of the item identifier.
     */
    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
