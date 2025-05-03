package main.se.kth.iv1350.rassjo.pos.application;

import main.se.kth.iv1350.rassjo.pos.integration.CashRegister;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.ReceiptPrinter;
import main.se.kth.iv1350.rassjo.pos.model.CashPayment;
import main.se.kth.iv1350.rassjo.pos.model.Receipt;

/**
 * The PaymentService class is responsible for processing and finalizing payments
 * for a given sale. It interacts with the associated `CashRegister` to update its
 * balance and uses the `ReceiptPrinter` to print sales receipts.
 */
public class PaymentService {

    private final CashRegister cashRegister;
    private final ReceiptPrinter receiptPrinter;

    /**
     * Creates a new instance of the {@code PaymentService} class.
     * This constructor initializes the service by creating and associating
     * a new {@code CashRegister} and a new {@code ReceiptPrinter}.
     * The {@code CashRegister} is responsible for managing cash transactions
     * and the running balance of the cash register, while the {@code ReceiptPrinter}
     * handles printing of sales receipts.
     */
    public PaymentService() {
        cashRegister = new CashRegister();
        receiptPrinter = new ReceiptPrinter();
    }

    /**
     * Processes the payment for a sale by updating the cash register,
     * generating a receipt, and printing it using the receipt printer.
     *
     * @param payment the {@code CashPayment} object containing information
     *                about the payment.
     * @param saleInformation the {@code SaleDTO} object providing details
     *                        about the sale.
     */
    public void processPayment(CashPayment payment, SaleDTO saleInformation) {
        cashRegister.addPayment(payment);
        Receipt receipt = new Receipt(saleInformation, payment);
        receiptPrinter.printReceipt(receipt);
    }
}
