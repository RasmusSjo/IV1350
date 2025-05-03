package main.se.kth.iv1350.rassjo.pos.application;

import main.se.kth.iv1350.rassjo.pos.integration.CashRegister;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.ReceiptPrinter;
import main.se.kth.iv1350.rassjo.pos.model.CashPayment;

public class PaymentService {

    private final CashRegister cashRegister;
    private final ReceiptPrinter receiptPrinter;

    public PaymentService() {
        cashRegister = new CashRegister();
        receiptPrinter = new ReceiptPrinter();
    }

    public AmountDTO processPayment(CashPayment payment, SaleDTO saleInformation) {
        return null;
    }
}
