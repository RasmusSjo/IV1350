package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents a cash payment transaction, containing details about the total cost,
 * the amount paid by the customer, and the change to be returned.
 *
 * @param totalCost  the total cost of the transaction.
 * @param paidAmount the amount paid by the customer.
 * @param change     the change to be returned to the customer.
 */
public record CashPaymentDTO(AmountDTO totalCost, AmountDTO paidAmount, AmountDTO change) {
}
