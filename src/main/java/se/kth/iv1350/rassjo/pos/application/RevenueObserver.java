package se.kth.iv1350.rassjo.pos.application;

import se.kth.iv1350.rassjo.pos.integration.DTOs.CashPaymentDTO;

/**
 * Defines an observer interface for receiving notifications about revenue updates
 * when a cash payment is processed in the system.
 */
public interface RevenueObserver {

    /**
     * Notifies the observer that a cash payment has been received. The observer will
     * update the total revenue and print the total revenue.
     *
     * @param payment the {@link CashPaymentDTO} object containing details of the cash payment.
     */
    void paymentReceived(CashPaymentDTO payment);
}
