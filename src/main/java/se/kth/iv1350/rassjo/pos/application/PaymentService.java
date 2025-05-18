package se.kth.iv1350.rassjo.pos.application;

import se.kth.iv1350.rassjo.pos.integration.DTOs.Mapper;
import se.kth.iv1350.rassjo.pos.integration.ReceiptPrinter;
import se.kth.iv1350.rassjo.pos.model.CashPayment;
import se.kth.iv1350.rassjo.pos.model.CashRegisterTracker;
import se.kth.iv1350.rassjo.pos.integration.DTOs.ReceiptDTO;
import se.kth.iv1350.rassjo.pos.model.Sale;

/**
 * The PaymentService class is responsible for processing and finalizing payments
 * for a given sale. It interacts with the associated `CashRegister` to update its
 * balance and uses the `ReceiptPrinter` to print sales receipts.
 */
public class PaymentService {

    private final CashRegisterTracker cashRegisterTracker;
    private final ReceiptPrinter receiptPrinter;

    /**
     * Creates an instance of the PaymentService.
     *
     * @param receiptPrinter the {@link ReceiptPrinter} responsible for printing
     *                       receipts for processed payments.
     */
    public PaymentService(ReceiptPrinter receiptPrinter) {
        this.cashRegisterTracker = new CashRegisterTracker();
        this.receiptPrinter = receiptPrinter;
    }

    /**
     * Processes the payment for the given sale.
     * <p>
     * This will update the cash register, dispense any change,
     * and print a receipt.
     *
     * @param sale    the {@link Sale} object providing details about the sale.
     * @param payment the {@link CashPayment} object containing information
     *                about the payment.
     */
    public void processPayment(Sale sale, CashPayment payment) {
        // Here there would be logic for calling the actual cash register, not just its tracker
        cashRegisterTracker.addPayment(payment);
        cashRegisterTracker.dispenseChange(payment.getPaidAmount());

        ReceiptDTO receipt = new ReceiptDTO(Mapper.toDTO(sale), Mapper.toDTO(payment));
        receiptPrinter.printReceipt(receipt);
    }
}
