package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents a monetary amount as a string.
 *
 * @param amount the amount in SEK.
 */
public record AmountDTO(String amount) {

    /**
     * Converts the monetary amount into a string representation, separating the integer
     * and decimal parts with a colon.
     *
     * @return the formatted string representation of the monetary amount.
     */
    @Override
    public String toString() {
        String[] parts = amount.split("\\.");
        return String.format("%s:%s", parts[0], parts[1]);
    }
}
