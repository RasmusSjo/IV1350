package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

/**
 * A monetary amount in Swedish Krona (SEK).
 */
public class Amount {

    private double amount;

    /**
     * Creates a new Amount with the specified initial value.
     *
     * @param amount the initial value.
     */

    public Amount(double amount) {
        this.amount = amount;
    }

    /**
     * Creates a new Amount from an AmountDTO.
     *
     * @param amount the DTO containing the monetary value
     */
    public Amount(AmountDTO amount) {
        this.amount = amount.amount();
    }

    /**
     * Returns the current monetary value.
     *
     * @return the amount as a double
     */

    public double getAmount() {
        return amount;
    }

    /**
     * Increases this monetary amount by the value of the given amount.
     *
     * @param amount the {@code Amount} representing the monetary value to add.
     */
    public void increaseAmountBySum(Amount amount) {
        this.amount += amount.getAmount();
    }

    /**
     * Increases this monetary amount by the value of the given amount.
     *
     * @param amount the {@code AmountDTO} representing the monetary value to add.
     */
    public void increaseAmountBySum(AmountDTO amount) {
        this.amount += amount.amount();
    }

    /**
     * Decreases this monetary amount by the value of the given amount.
     *
     * @param amount the {@code Amount} representing the monetary value to subtract
     */
    public void decreaseAmountBySum(Amount amount) {
        this.amount -= amount.getAmount();
    }

    /**
     * Decreases this monetary amount by the value of the given amount.
     *
     * @param amount the {@code AmountDTO} representing the monetary value to subtract.
     */
    public void decreaseAmountBySum(AmountDTO amount) {
        this.amount -= amount.amount();
    }

    /**
     * Increases the monetary amount by the specified percentage.
     *
     * @param percentage the {@code PercentageDTO} containing the percentage to apply (e.g., 10 for a 10% increase).
     */
    public void increaseAmountByPercentage(PercentageDTO percentage) {
        this.amount *= (1 + (double) percentage.percentage() / 100);
    }

    /**
     * Decreases this monetary amount by a given percentage.
     *
     * @param percentage the {@code PercentageDTO} containing the percentage to subtract (e.g., 25 for a 25% decrease)
     */
    public void decreaseAmountByPercentage(PercentageDTO percentage) {
        this.amount *= (1 - (double) percentage.percentage() / 100);
    }

    /**
     * Converts the monetary amount into a string representation, separating the integer
     * and decimal parts with a colon. Ensures that the decimal part always contains two
     * digits by appending a zero if necessary.
     *
     * @return a string representation of the amount in the format "integerPart:decimalPart".
     */
    @Override
    public String toString() {
        AmountDTO amountForString = new AmountDTO(this.amount);
        return amountForString.toString();
    }
}
