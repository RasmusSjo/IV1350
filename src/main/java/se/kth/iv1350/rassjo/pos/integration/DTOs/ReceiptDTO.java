package se.kth.iv1350.rassjo.pos.integration.DTOs;

import java.time.format.DateTimeFormatter;

/**
 * Represents a receipt for a completed sale. Contains information about the sale (e.g. items,
 * start time, total cost, total VAT).
 * </p>
 * Has a {@link #toString} method for formatting the receipt to a printable format.
 */
public class ReceiptDTO {

    private static final int RECEIPT_WIDTH = 56;
    private static final int ITEM_QUANTITY_PLACEMENT = 28;
    private static final int SUMMARY_PLACEMENT = 40;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int RIGHT_PADDING = 2;
    private static final String ITEM_LINE_FORMAT =
            "%-" + ITEM_QUANTITY_PLACEMENT + "s " +
            "%-" + (SUMMARY_PLACEMENT - ITEM_QUANTITY_PLACEMENT) + "s " +
            "%" + (RECEIPT_WIDTH - SUMMARY_PLACEMENT - RIGHT_PADDING) + "s\n";
    private static final String SUMMARY_LINE_FORMAT =
            "%-" + SUMMARY_PLACEMENT + "s %" + (RECEIPT_WIDTH - SUMMARY_PLACEMENT - RIGHT_PADDING) + "s\n";

    private final SaleDTO sale;
    private final CashPaymentDTO payment;

    /**
     * Creates an instance of {@link ReceiptDTO}, encapsulating information about a sale
     * and its associated cash payment details.
     *
     * @param saleInformation {@link SaleDTO} object containing information about the sale.
     * @param payment         {@link CashPaymentDTO} containing details about the payment made for the sale.
     */
    public ReceiptDTO(SaleDTO saleInformation, CashPaymentDTO payment) {
        this.sale = saleInformation;
        this.payment = payment;
    }

    /**
     * Generates a formatted string representation of a receipt. The receipt includes a header,
     * sale start time, details of purchased items, totals (including VAT), payment details,
     * and a footer.
     *
     * @return a detailed, formatted string representation of the receipt.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String beginReceipt = " Begin receipt ";
        int topSidePadding = (RECEIPT_WIDTH - beginReceipt.length() + 1) / 2;

        // Header
        sb.append("-".repeat(topSidePadding)).append(beginReceipt).append("-".repeat(topSidePadding)).append("\n");

        // Time of sale
        sb.append("Time of Sale: ").append(sale.startTime().format(TIME_FORMAT)).append("\n\n");

        // Items
        for (SaleItemDTO item : sale.items()) {
            String quantityAndPrice = item.quantity() + " x " + item.finalUnitPrice();
            sb.append(String.format(ITEM_LINE_FORMAT, item.name(), quantityAndPrice, item.finalTotalPrice() + " SEK"));
        }
        sb.append("\n");

        // Totals
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Total:", sale.totalCost() + " SEK"));
        sb.append("VAT: ").append(sale.totalVat()).append("\n\n");

        // Payment
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Payment:", payment.paidAmount() + " SEK"));
        sb.append(String.format(SUMMARY_LINE_FORMAT, "Change:", payment.change() + " SEK"));

        // Footer
        String endReceipt = " End receipt ";
        int bottomSidePadding = (RECEIPT_WIDTH - endReceipt.length() + 1) / 2;
        sb.append("-".repeat(bottomSidePadding)).append(" End receipt ").append("-".repeat(bottomSidePadding));

        return sb.toString();
    }
}
