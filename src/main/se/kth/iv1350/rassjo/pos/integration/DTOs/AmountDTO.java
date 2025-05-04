package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import java.math.BigDecimal;

/**
 * Represents an amount in Swedish Krona (SEK).
 *
 * @param amount the amount in SEK.
 */
public record AmountDTO(BigDecimal amount) {

    /**
     * Converts the monetary amount into a string representation, separating the integer
     * and decimal parts with a colon. Ensures that the decimal part always contains two
     * digits.
     *
     * @return a string representation of the amount in the format "integerPart:decimalPart".
     */
    @Override
    public String toString() {
        String amountStr = amount.toPlainString();
        String[] parts = amountStr.split("\\.");
        return String.format("%s:%s", parts[0], parts[1]);
    }
}
