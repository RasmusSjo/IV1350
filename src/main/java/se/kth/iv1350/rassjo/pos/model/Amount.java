package se.kth.iv1350.rassjo.pos.model;

import se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A monetary amount in Swedish Krona (SEK). This value is immutable.
 */
public class Amount {

    private static final String DEFAULT_VALUE = "0.00";
    private static final int HUNDRED_PERCENT = 100;
    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final BigDecimal amount;

    /**
     * Creates a new {@link Amount} instance with a default value.
     * <p>
     * The default value is 0 SEK.
     */
    Amount() {
        this(DEFAULT_VALUE);
    }

    /**
     * Creates a new {@link Amount} instance with the specified integer initial value.
     *
     * @param amount the initial value as an integer.
     */
    Amount(int amount) {
        this(BigDecimal.valueOf(amount));
    }

    /**
     * Creates a new {@link Amount} instance by copying the value from another {@code Amount} instance.
     *
     * @param amount the {@code Amount} instance whose value will be copied to the new {@code Amount} instance.
     */
    Amount(Amount amount) {
        this(amount.getAmount());
    }

    /**
     * Creates a new {@link Amount} instance with the specified initial value.
     *
     * @param amount the initial value as a {@code String}. The string should
     *               represent a valid decimal number.
     */
    public Amount(String amount) {
        this(new BigDecimal(amount));
    }

    /**
     * Creates a new {@code Amount} instance with the specified {@code BigDecimal} value.
     * The value is formatted to have precisely two decimal places.
     *
     * @param amount the monetary value to initialize the {@code Amount} instance,
     *               represented as a {@code BigDecimal}.
     */
    private Amount(BigDecimal amount) {
        this.amount = formatAmount(amount);
    }

    /**
     * Retrieves the monetary amount.
     *
     * @return a {@code String} representing the value of the monetary amount in plain text.
     */
    public String getAmount() {
        return amount.toPlainString();
    }

    /**
     * Returns an {@link Amount} representing the sum of this
     * instance and the specified one.
     *
     * @param amount the {@code Amount} instance to add to this one.
     * @return a new {@code Amount} instance representing the result of the addition.
     */
    Amount add(Amount amount) {
        return new Amount(this.amount.add(amount.amount));
    }

    /**
     * Returns an {@link Amount} representing the difference between this
     * instance and the specified one.
     *
     * @param amount the {@code Amount} instance to subtract from this one.
     * @return a new {@code Amount} instance representing the result of the substraction.
     */
    Amount subtract(Amount amount) {
        return new Amount(this.amount.subtract(amount.amount));
    }

    /**
     * Returns an {@link Amount} representing this amount multiplied by the specified quantity
     *
     * @param quantity the quantity by which the current {@code Amount} is to be multiplied.
     * @return a new {@code Amount} instance representing the result of the multiplication.
     */
    Amount multiplyByQuantity(int quantity) {
        return new Amount(this.amount.multiply(BigDecimal.valueOf(quantity)));
    }

    /**
     * Increases the current monetary amount by a given percentage.
     *
     * @param percentage the {@code PercentageDTO} containing the percentage to add
     *                   (e.g., 25 for a 25% increase).
     * @return a new {@code Amount} instance representing the increased monetary value.
     */
    Amount increaseBy(PercentageDTO percentage) {
        return new Amount(this.amount.multiply(toIncreaseFactor(percentage)));
    }

    /**
     * Reduces the current monetary amount by the given percentage.
     *
     * @param percentage the {@code PercentageDTO} containing the percentage to decrease
     *                   (e.g., 25 for a 25% reduction).
     * @return a new {@code Amount} instance representing the reduced monetary value.
     */
    Amount decreaseBy(PercentageDTO percentage) {
        return new Amount(this.amount.multiply(toDecreaseFactor(percentage)));
    }

    /**
     * Two {@code Amount}s are equal if they represent the same monetary value.
     *
     * @param o the reference object with which to compare.
     * @return {@code true} if {@code o} is of the {@code Amount} type and has
     *          the same monetary value as this instance, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Amount other)) return false;
        return this.amount.compareTo(other.amount) == 0;
    }

    /**
     * Returns the hash code for this {@code Amount} instance. The hash code is based on
     * the normalized value of the monetary amount, where trailing zeros are stripped.
     *
     * @return an integer representing the hash code of the normalized amount.
     */
    @Override
    public int hashCode() {
        BigDecimal normalized = amount.stripTrailingZeros();
        return normalized.hashCode();
    }

    private BigDecimal toIncreaseFactor(PercentageDTO percentage) {
        return BigDecimal.valueOf(1 + (double) percentage.percentage() / HUNDRED_PERCENT);
    }

    private BigDecimal toDecreaseFactor(PercentageDTO percentage) {
        return BigDecimal.valueOf(1 - (double) percentage.percentage() / HUNDRED_PERCENT);
    }

    private BigDecimal formatAmount(BigDecimal amount) {
        return amount.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }
}
