package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import main.se.kth.iv1350.rassjo.pos.model.CashPayment;
import main.se.kth.iv1350.rassjo.pos.model.SaleStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sale.
 *
 * @param startTime     the date and time when the sale was initiated.
 * @param totalCost     the total amount charged for the sale, including VAT and discounts.
 * @param totalVat      the total VAT amount collected for the sale.
 * @param items         the list of individual items sold in this sale.
 * @param lastAddedItem the last item added to the sale.
 * @param payment       the cash payment details provided by the customer.
 * @param status        the current status of the sale (e.g. OPEN, PAID, COMPLETED).
 */
public record SaleDTO(LocalDateTime startTime, AmountDTO totalCost, AmountDTO totalVat, List<SaleItemDTO> items,
                      SaleItemDTO lastAddedItem, CashPayment payment, SaleStatus status) {
}
