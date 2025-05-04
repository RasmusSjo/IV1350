package se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Encapsulates all applicable discounts for a sale.
 *
 * @param discountAmount             the fixed amount to subtract from the total sale price.
 * @param saleDiscountPercentage     the percentage reduction based on the total cost of the sale.
 * @param customerDiscountPercentage the percentage reduction based on the customer.
 */
public record DiscountDTO(AmountDTO discountAmount, PercentageDTO saleDiscountPercentage,
                          PercentageDTO customerDiscountPercentage) {
}





