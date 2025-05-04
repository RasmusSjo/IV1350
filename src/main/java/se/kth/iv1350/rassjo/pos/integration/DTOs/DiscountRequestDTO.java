package se.kth.iv1350.rassjo.pos.integration.DTOs;

import java.util.List;

/**
 * Represents the data needed for the discount database to calculate applicable discounts for a sale.
 *
 * @param customerId the identifier of the customer making the purchase.
 * @param totalPrice the total sale price before any discounts are applied.
 * @param items      the list of items included in the sale.
 */
public record DiscountRequestDTO(CustomerIdentifierDTO customerId, AmountDTO totalPrice, List<SaleItemDTO> items) {
}
