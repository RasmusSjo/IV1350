package se.kth.iv1350.rassjo.pos.model;

import se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.ItemDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

/**
 * Represents an item within a sale. Contains information about the purchased item,
 * its final price in the sale (including VAT and potential discounts), its final
 * total price (quantity adjusted), as well as the quantity purchased.
 */
public class SaleItem {

    private final ItemDTO itemInformation;
    private final Amount finalUnitPrice;
    private Amount finalTotalPrice;
    private int quantity;

    /**
     * Creates a new {@link SaleItem} instance, representing a purchased item within a sale.
     *
     * @param item      the {@link ItemDTO} containing static information about the item.
     * @param quantity  the number of items of this type that are being purchased.
     */
    SaleItem(ItemDTO item, int quantity) {
        this.itemInformation = item;
        finalUnitPrice = new Amount(item.baseNetPrice().amount()).increaseBy(item.vatRate());
        finalTotalPrice = new Amount(finalUnitPrice.multiplyByQuantity(quantity));
        this.quantity = quantity;
    }

    /**
     * Retrieves the unique identifier for this sale item.
     *
     * @return an {@link ItemIdentifierDTO} representing the identifier of the item.
     */
    public ItemIdentifierDTO getId() {
        return itemInformation.itemId();
    }

    /**
     * Retrieves the name of this sale item.
     *
     * @return the name of the item
     */
    public String getName() {
        return itemInformation.name();
    }

    /**
     * Retrieves the description of this sale item.
     *
     * @return the item's description
     */
    public String getDescription() {
        return itemInformation.description();
    }

    /**
     * Retrieves the base net price of this sale item.
     *
     * @return an {@link AmountDTO} object representing the net price of the item.
     */
    public AmountDTO getBaseNetPrice() {
        return itemInformation.baseNetPrice();
    }

    /**
     * Retrieves the VAT rate associated with this sale item.
     *
     * @return a {@link PercentageDTO} representing the VAT rate of the item.
     */
    public PercentageDTO getVatRate() {
        return itemInformation.vatRate();
    }

    /**
     * Retrieves the final price of this sale item. The final price includes VAT
     * and any potential adjustments applied during the sale (e.g., by discounts).
     *
     * @return an {@link Amount} object representing the final price of the item.
     */
    public Amount getFinalUnitPrice() {
        return finalUnitPrice;
    }

    /**
     * Retrieves the final total price of this sale item. The final total price is
     * calculated as the product of the final unit price and the quantity of the item.
     *
     * @return an {@link Amount} representing the final total price of this sale item.
     */
    public Amount getFinalTotalPrice() {
        return finalTotalPrice;
    }

    /**
     * Retrieves the quantity of this sale item.
     *
     * @return the quantity of the item as an integer
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Increases the quantity of the sale item by the specified amount.
     *
     * @param quantity the amount to increase the current quantity by
     */
    void increaseQuantity(int quantity) {
        this.quantity += quantity;
        finalTotalPrice = finalTotalPrice.add(finalUnitPrice.multiplyByQuantity(quantity));
    }
}
