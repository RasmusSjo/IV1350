package main.se.kth.iv1350.rassjo.pos.model;

/**
 * Tracks the balance of the actual cash register in the POS system. This includes updating
 * the cash register balance with incoming payments and outgoing change.
 */
public class CashRegisterTracker {

    private static final int STARTUP_BALANCE = 1000;
    private Amount currentBalance;

    /**
     * Initializes a new {@link CashRegisterTracker} instance with a predefined startup balance.
     */
    public CashRegisterTracker() {
        currentBalance = new Amount(STARTUP_BALANCE);
    }

    /**
     * Updates the current balance with the paid amount.
     *
     * @param payment the {@link CashPayment} containing the paid amount to be added.
     */
    public void addPayment(CashPayment payment) {
         currentBalance = currentBalance.add(payment.getPaidAmount());
    }

    /**
     * Updates the current balance with the dispensed change.
     *
     * @param amount the {@link Amount} representing the monetary that's been dispensed.
     */
    public void dispenseChange(Amount amount) {
        currentBalance = currentBalance.subtract(amount);
    }
}
