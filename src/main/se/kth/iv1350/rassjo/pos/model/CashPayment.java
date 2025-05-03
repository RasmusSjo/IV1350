package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.Mapper;

/**
 * Represents a cash payment where the total cost, paid amount,
 * and any change to be returned are handled.
 */
public class CashPayment {

    private final AmountDTO totalCost;
    private Amount paidAmount;
    private Amount change;

    /**
     * Creates an instance of {@code CashPayment}, representing a cash payment transaction.
     *
     * @param totalCost the total cost of the transaction as an {@code AmountDTO}.
     * @param paidAmount the amount paid by the customer as an {@code AmountDTO}.
     */
    public CashPayment(AmountDTO totalCost, AmountDTO paidAmount) {
        this.totalCost = totalCost;
        this.paidAmount = new Amount(paidAmount);
    }

    /**
     * Retrieves the total cost involved in the transaction.
     *
     * @return an {@code AmountDTO} object representing the total cost in SEK.
     */
    public AmountDTO getTotalCost() {
        return totalCost;
    }

    /**
     * Retrieves the paid amount as an {@link AmountDTO}.
     *
     * @return an {@link AmountDTO} representing the amount paid.
     */
    public AmountDTO getPaidAmount() {
        return Mapper.toDTO(paidAmount);
    }

    /**
     * Retrieves the change as an {@link AmountDTO}.
     *
     * @return an {@link AmountDTO} representing the change calculated from the payment.
     */
    public AmountDTO getChange() {
        return Mapper.toDTO(change);
    }

    private void calculateChange() {
        change = new Amount(totalCost.amount() - paidAmount.getAmount());
    }
}
