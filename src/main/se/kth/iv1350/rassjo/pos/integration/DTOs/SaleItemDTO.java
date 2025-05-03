package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents an item in a sale, including information about the corresponding inventory item.
 *
 * @param id          the unique identifier of the item in the inventory system.
 * @param name        the name of the item.
 * @param description a brief description of the item.
 * @param unitPrice   the price per unit before VAT and discounts.
 * @param vatRate     the VAT percentage applied to the item.
 * @param finalPrice  the price per unit after applying VAT and any potential discounts.
 * @param quantity    the number of units of this item in the sale.
 */
public record SaleItemDTO(ItemIdentifierDTO id, String name, String description, AmountDTO unitPrice,
                          PercentageDTO vatRate, AmountDTO finalPrice, int quantity) {
}
