package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleItemDTO;

import java.time.format.DateTimeFormatter;

public class Receipt {

    private static final int RECEIPT_WIDTH = 80;
    private static final int ITEM_QUANTITY_PLACEMENT = 50;
    private static final int SUMMARY_PLACEMENT = 70;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String ITEM_LINE_FORMAT =
            "%-" + ITEM_QUANTITY_PLACEMENT + "s %-" + (SUMMARY_PLACEMENT - ITEM_QUANTITY_PLACEMENT) + "s";
    private static final String SUMMARY_LINE_FORMAT = "%-" + SUMMARY_PLACEMENT + "s";

    private final CashPayment payment;
    private final SaleDTO saleInformation;

    public Receipt(SaleDTO saleInformation, CashPayment payment) {
        this.saleInformation = saleInformation;
        this.payment = payment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String beginReceipt = " Begin receipt ";
        int topSidePadding = (RECEIPT_WIDTH - beginReceipt.length()) / 2;

        // Header
        sb.append("-".repeat(topSidePadding)).append(beginReceipt).append("-".repeat(topSidePadding));

        // Time of sale
        sb.append("Time of Sale: ").append(saleInformation.startTime().format(TIME_FORMAT)).append("\n\n");

        // Items
        for (SaleItemDTO item : saleInformation.items()) {
            Amount totalItemCost = new Amount(item.quantity() * item.finalPrice().amount());
            String quantityAndPrice = item.quantity() + " x " + item.netPrice();
            sb.append(String.format(ITEM_LINE_FORMAT, item.name(), quantityAndPrice));
            sb.append(totalItemCost).append(" SEK\n");
        }
        sb.append("\n");

        // Totals
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Total:")).append(saleInformation.totalCost()).append(" SEK\n");
        sb.append("VAT: ").append(saleInformation.totalVat()).append(" SEK\n\n");

        // Payment
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Payment:")).append(payment.getPaidAmount()).append(" SEK\n");
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Change:")).append(payment.getChange()).append(" SEK\n");

        // Footer
        String endReceipt = " End receipt ";
        int bottomSidePadding = (RECEIPT_WIDTH - endReceipt.length()) / 2;
        sb.append("-".repeat(bottomSidePadding)).append(" End receipt ").append("-".repeat(bottomSidePadding));

        return sb.toString();
    }
}
