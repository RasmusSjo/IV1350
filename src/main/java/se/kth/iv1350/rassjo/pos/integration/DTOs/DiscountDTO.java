package se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Encapsulates all applicable discounts for a sale.
 *
 * @param itemDiscountAmount         the fixed amount reduction based on the items in the sale.
 * @param saleDiscountPercentage     the percentage reduction based on the total cost of the sale.
 * @param customerDiscountPercentage the percentage reduction based on the customer.
 */
public record DiscountDTO(AmountDTO itemDiscountAmount, PercentageDTO saleDiscountPercentage,
                          PercentageDTO customerDiscountPercentage) {
}





