package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents an amount in Swedish Krona (SEK).
 *
 * @param amount the amount in SEK.
 */
public record AmountDTO(double amount) {

    /**
     * Converts the monetary amount into a string representation, separating the integer
     * and decimal parts with a colon. Ensures that the decimal part always contains two
     * digits.
     *
     * @return a string representation of the amount in the format "integerPart:decimalPart".
     */
    @Override
    public String toString(){
        int integerPart = (int) amount;
        int decimalPart = (int) ((amount - integerPart) * 100);
        decimalPart = decimalPart < 0 ? -decimalPart : decimalPart;
        decimalPart = decimalPart < 10 ? decimalPart * 10 : decimalPart;
        return String.format("%d:%02d", integerPart, decimalPart);
    }
}
