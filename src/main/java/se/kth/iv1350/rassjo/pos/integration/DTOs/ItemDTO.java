package se.kth.iv1350.rassjo.pos.integration.DTOs;

/**
 * Represents the static information for an item as retrieved from the external inventory system.
 *
 * <p>When a cashier enters an item identifier, this DTO carries the item’s name,
 * description, net price (before VAT), and applicable VAT rate returned by the inventory system.</p>
 *
 * @param itemId      the unique identifier of the item in the inventory system.
 * @param name        the name of the item.
 * @param description a brief description of the item.
 * @param baseNetPrice    the price per unit before VAT.
 * @param vatRate     the VAT percentage applied to the item.
 */
public record ItemDTO(ItemIdentifierDTO itemId, String name, String description, AmountDTO baseNetPrice,
                      PercentageDTO vatRate) {
}
