package main.se.kth.iv1350.rassjo.pos.application;

import main.se.kth.iv1350.rassjo.pos.integration.CashRegister;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.ReceiptPrinter;
import main.se.kth.iv1350.rassjo.pos.model.CashPayment;
import main.se.kth.iv1350.rassjo.pos.model.Receipt;

public class PaymentService {

    private final CashRegister cashRegister;
    private final ReceiptPrinter receiptPrinter;

    public PaymentService() {
        cashRegister = new CashRegister();
        receiptPrinter = new ReceiptPrinter();
    }

    public void processPayment(CashPayment payment, SaleDTO saleInformation) {
        cashRegister.addPayment(payment);
        Receipt receipt = new Receipt(saleInformation, payment);
        receiptPrinter.printReceipt(receipt);
    }
}
