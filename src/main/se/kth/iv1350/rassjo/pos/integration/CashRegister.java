package main.se.kth.iv1350.rassjo.pos.integration;

import main.se.kth.iv1350.rassjo.pos.model.Amount;
import main.se.kth.iv1350.rassjo.pos.model.CashPayment;

/**
 * Represents a cash register used to track transactions
 * and maintain a running balance. It allows adding payments
 * and dispensing change.
 */
public class CashRegister {

    private static final double STARTUP_BALANCE = 1000;
    private final Amount currentBalance;

    /**
     * Initializes a new instance of the CashRegister class with a predefined startup balance.
     */
    public CashRegister() {
        currentBalance = new Amount(STARTUP_BALANCE);
    }

    /**
     * Adds a cash payment to the cash register and updates the current balance
     * with the paid amount.
     *
     * @param payment the {@code CashPayment} containing the paid amount to be added.
     */
    public void addPayment(CashPayment payment) {
        currentBalance.increaseAmountBySum(payment.getPaidAmount());
    }

    /**
     * Dispenses the specified monetary amount as change by reducing the current balance
     * of the cash register by the given amount.
     *
     * @param amount the {@code Amount} representing the monetary value to be dispensed as change.
     */
    public void dispenseChange(Amount amount) {
        currentBalance.decreaseAmountBySum(amount);
    }
}
