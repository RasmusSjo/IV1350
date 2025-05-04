package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleItemDTO;

import java.time.format.DateTimeFormatter;

public class Receipt {

    private static final int RECEIPT_WIDTH = 56;
    private static final int ITEM_QUANTITY_PLACEMENT = 28;
    private static final int SUMMARY_PLACEMENT = 40;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String ITEM_LINE_FORMAT =
            "%-" + ITEM_QUANTITY_PLACEMENT + "s " +
            "%-" + (SUMMARY_PLACEMENT - ITEM_QUANTITY_PLACEMENT) + "s " +
            "%" + (RECEIPT_WIDTH - SUMMARY_PLACEMENT - 2) + "s\n";
    private static final String SUMMARY_LINE_FORMAT =
            "%-" + SUMMARY_PLACEMENT + "s %" + (RECEIPT_WIDTH - SUMMARY_PLACEMENT - 2) + "s\n";

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
        int topSidePadding = (RECEIPT_WIDTH - beginReceipt.length() + 1) / 2;

        // Header
        sb.append("-".repeat(topSidePadding)).append(beginReceipt).append("-".repeat(topSidePadding)).append("\n");

        // Time of sale
        sb.append("Time of Sale: ").append(saleInformation.startTime().format(TIME_FORMAT)).append("\n\n");

        // Items
        for (SaleItemDTO item : saleInformation.items()) {
            Amount totalItemCost = new Amount(item.quantity() * item.finalPrice().amount());
            String quantityAndPrice = item.quantity() + " x " + item.netPrice();
            sb.append(String.format(ITEM_LINE_FORMAT, item.name(), quantityAndPrice, totalItemCost + " SEK"));
        }
        sb.append("\n");

        // Totals
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Total:", saleInformation.totalCost() + " SEK"));
        sb.append("VAT: ").append(saleInformation.totalVat()).append("\n\n");

        // Payment
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Payment:", payment.getPaidAmount() + " SEK"));
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Change:", payment.getChange() + " SEK"));

        // Footer
        String endReceipt = " End receipt ";
        int bottomSidePadding = (RECEIPT_WIDTH - endReceipt.length() + 1) / 2;
        sb.append("-".repeat(bottomSidePadding)).append(" End receipt ").append("-".repeat(bottomSidePadding));

        return sb.toString();
    }
}
