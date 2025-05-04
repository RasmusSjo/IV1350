package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A monetary amount in Swedish Krona (SEK).
 */
public class Amount {

    private static final int HUNDRED_PERCENT = 100;
    private static final int DECIMAL_PLACES = 2;
    private double amount;

    /**
     * Creates a new Amount with the specified initial value.
     *
     * @param amount the initial value.
     */

    public Amount(double amount) {
        this.amount = amount;
        formatAmount();
    }

    /**
     * Creates a new Amount from an Amount.
     *
     * @param amount the {@code Amount} containing the monetary value
     */
    public Amount(Amount amount) {
        this.amount = amount.getAmount();
        formatAmount();
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
        updateAmount(this.amount + amount.getAmount());
    }

    /**
     * Decreases this monetary amount by the value of the given amount.
     *
     * @param amount the {@code Amount} representing the monetary value to subtract
     */
    public void decreaseAmountBySum(Amount amount) {
        updateAmount(this.amount - amount.getAmount());
    }

    /**
     * Increases the monetary amount by the specified percentage.
     *
     * @param percentage the {@code PercentageDTO} containing the percentage to apply (e.g., 10 for a 10% increase).
     */
    public void increaseAmountByPercentage(PercentageDTO percentage) {
        updateAmount(this.amount * (1 + (double) percentage.percentage() / HUNDRED_PERCENT));
    }

    /**
     * Decreases this monetary amount by a given percentage.
     *
     * @param percentage the {@code PercentageDTO} containing the percentage to subtract (e.g., 25 for a 25% decrease)
     */
    public void decreaseAmountByPercentage(PercentageDTO percentage) {
        updateAmount(this.amount * (1 - (double) percentage.percentage() / HUNDRED_PERCENT));
    }

    private void updateAmount(double amount){
        this.amount = amount;
        formatAmount();
    }

    /**
     * Utility method for rounding the amount to two decimal places. Retrieved from
     * <a href="https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places">here<a/>.
     */
    private void formatAmount() {
        BigDecimal bd = BigDecimal.valueOf(amount);
        bd = bd.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
        amount = bd.doubleValue();
    }
}
