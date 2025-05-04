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

    private final CashRegisterTracker cashRegisterHandler;
    private final ReceiptPrinter receiptPrinter;

    /**
     * Creates a new instance of the {@code PaymentService} class.
     * This constructor initializes the service by creating and associating
     * a new {@code CashRegister} and a new {@code ReceiptPrinter}.
     * The {@code CashRegister} is responsible for managing cash transactions
     * and the running balance of the cash register, while the {@code ReceiptPrinter}
     * handles printing of sales receipts.
     */
    public PaymentService(ReceiptPrinter receiptPrinter) {
        this.cashRegisterHandler = new CashRegisterTracker();
        this.receiptPrinter = receiptPrinter;
    }

    /**
     * Processes the payment for a sale by updating the cash register,
     * generating a receipt, and printing it using the receipt printer.
     *
     * @param sale    the {@code Sale} object providing details about the sale.
     * @param payment the {@code CashPayment} object containing information
     *                about the payment.
     */
    public void processPayment(Sale sale, CashPayment payment) {
        // Here there would be logic for calling the actual cash register, not just its tracker
        cashRegisterHandler.addPayment(payment);
        cashRegisterHandler.dispenseChange(payment.getPaidAmount());

        ReceiptDTO receipt = new ReceiptDTO(Mapper.toDTO(sale), Mapper.toDTO(payment));
        receiptPrinter.printReceipt(receipt);
    }
}
