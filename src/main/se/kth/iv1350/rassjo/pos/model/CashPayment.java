package main.se.kth.iv1350.rassjo.pos.model;

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
     * @param totalCost the {@code Amount} object representing the total cost.
     * @param paidAmount the {@code Amount} object representing the amount paid by the customer.
     */
    public CashPayment(Amount totalCost, Amount paidAmount) {
        this.totalCost = totalCost;
        this.paidAmount = new Amount(paidAmount);
        change = calculateChange();
    }

    /**
     * Retrieves the total cost involved in the transaction.
     *
     * @return an {@code Amount} object representing the total cost in SEK.
     */
    public Amount getTotalCost() {
        return totalCost;
    }

    /**
     * Retrieves the paid amount as an {@link Amount}.
     *
     * @return an {@link Amount} representing the amount paid.
     */
    public Amount getPaidAmount() {
        return paidAmount;
    }

    /**
     * Retrieves the change as an {@link Amount}.
     *
     * @return an {@link Amount} representing the change calculated from the payment.
     */
    public Amount getChange() {
        return change;
    }

    private Amount calculateChange() {
        return new Amount(paidAmount.getAmount() - totalCost.getAmount());
    }
}
