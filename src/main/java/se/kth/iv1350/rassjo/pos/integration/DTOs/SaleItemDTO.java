package se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents an item in a sale, including information about the corresponding inventory item, the final
 * price of the item for the current sale (including VAT and potential discounts), the final total cost,
 * as well as the quantity purchased.
 *
 * @param id                the unique identifier of the item in the inventory system.
 * @param name              the name of the item.
 * @param description       a brief description of the item.
 * @param baseNetPrice      the price per unit before VAT and discounts.
 * @param vatRate           the VAT percentage applied to the item.
 * @param finalUnitPrice    the price per unit after applying VAT and any potential discounts.
 * @param quantity          the number of items of this type that in the sale.
 */
public record SaleItemDTO(ItemIdentifierDTO id, String name, String description, AmountDTO baseNetPrice,
                          PercentageDTO vatRate, AmountDTO finalUnitPrice, AmountDTO finalTotalPrice, int quantity) {
}
