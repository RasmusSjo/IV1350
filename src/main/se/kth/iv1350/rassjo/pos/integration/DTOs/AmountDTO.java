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
     * digits by appending a zero if necessary.
     *
     * @return a string representation of the amount in the format "integerPart:decimalPart".
     */
    @Override
    public String toString(){
        String doubleString = String.valueOf(amount);
        String[] split = doubleString.split("\\.");

        String integerPart = split[0];
        String decimalPart = split[1];

        if(decimalPart.length() == 1){
            decimalPart += "0";
        }
        else if (decimalPart.length() > 2){
            decimalPart = decimalPart.substring(0, 2);
        }

        return integerPart + ":" + decimalPart;
    }
}
