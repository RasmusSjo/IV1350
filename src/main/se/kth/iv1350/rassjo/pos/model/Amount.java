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
     * Increases this amount by the specified sum.
     *
     * @param amount the DTO representing the sum to add
     */

    public void increaseAmountBySum(AmountDTO amount) {
        this.amount += amount.amount();
    }

    /**
     * Decreases this amount by the specified sum.
     *
     * @param amount the DTO representing the sum to subtract
     */
    public void decreaseAmountBySum(AmountDTO amount) {
        this.amount -= amount.amount();
    }

    /**
     * Decreases the monetary value of this amount by the specified percentage.
     * <p>
     * The calculation subtracts the portion of this amount determined by
     * the given percentage DTO.
     * </p>
     *
     * @param percentage the DTO containing the percentage to apply (e.g., 10 for a 10% reduction).
     */
    public void decreaseAmountByPercentage(PercentageDTO percentage) {
        this.amount -= this.amount * (1 - (double) percentage.percentage() / 100);
    }
}
