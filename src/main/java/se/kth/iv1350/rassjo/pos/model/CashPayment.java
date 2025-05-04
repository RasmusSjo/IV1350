package se.kth.iv1350.rassjo.pos.model;

import java.util.Objects;

/**
 * Represents a cash payment where the total cost, paid amount,
 * and any change to be returned are handled.
 */
public class CashPayment {

    private final Amount totalCost;
    private final Amount paidAmount;
    private final Amount change;

    /**
     * Creates an instance of {@code CashPayment}, representing a cash payment transaction.
     *
     * @param totalCost  the {@code Amount} object representing the total cost.
     * @param paidAmount the {@code Amount} object representing the amount paid by the customer.
     */
    public CashPayment(Amount totalCost, Amount paidAmount) {
        this.totalCost = totalCost;
        this.paidAmount = new Amount(paidAmount);
        change = calculateChange(paidAmount, totalCost);
    }

    /**
     * Retrieves the total cost involved in the transaction.
     *
     * @return an {@link Amount} object representing the total cost in SEK.
     */
    public Amount getTotalCost() {
        return totalCost;
    }

    /**
     * Retrieves the paid amount in the transaction.
     *
     * @return an {@link Amount} representing the amount paid in SEK.
     */
    public Amount getPaidAmount() {
        return paidAmount;
    }

    /**
     * Retrieves the change for the transaction.
     *
     * @return an {@link Amount} representing the change calculated from the payment.
     */
    public Amount getChange() {
        return change;
    }

    private Amount calculateChange(Amount paidAmount, Amount totalCost) {
        return paidAmount.subtract(totalCost);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CashPayment that = (CashPayment) o;
        return Objects.equals(totalCost, that.totalCost) && Objects.equals(paidAmount, that.paidAmount) && Objects.equals(change, that.change);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCost, paidAmount, change);
    }
}
