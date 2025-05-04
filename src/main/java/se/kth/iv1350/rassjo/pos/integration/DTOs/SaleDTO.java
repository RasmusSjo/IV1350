package se.kth.iv1350.rassjo.pos.integration.DTOs;

import se.kth.iv1350.rassjo.pos.model.CashPayment;
import se.kth.iv1350.rassjo.pos.model.SaleStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sale.
 *
 * @param saleId          the unique identifier for the sale.
 * @param startTime       the timestamp indicating when the sale started.
 * @param totalCost       the total cost of the sale, including VAT.
 * @param totalVat        the total value-added tax (VAT) for the sale.
 * @param items           the list of items included in the sale.
 * @param lastAddedItem   the most recently added item to the sale.
 * @param payment         the cash payment made for the sale.
 * @param status          the current status of the sale, such as registering, awaiting payment, or paid.
 */
public record SaleDTO(String saleId, LocalDateTime startTime, AmountDTO totalCost, AmountDTO totalVat, List<SaleItemDTO> items,
                      SaleItemDTO lastAddedItem, CashPayment payment, SaleStatus status) {
}
