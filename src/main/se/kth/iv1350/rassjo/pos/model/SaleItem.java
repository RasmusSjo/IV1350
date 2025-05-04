package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

/**
 * Represents an item within a sale. Contains information about the purchased item,
 *  its final price in the sale (including VAT and potential discounts), as
 * well as the quantity purchased.
 */

public class SaleItem {

    private final ItemDTO itemInformation;
    private final Amount finalPrice;
    private int quantity;

    /**
     * Creates a new instance of SaleItem, representing a purchased item within a sale.
     * The SaleItem includes information about the item, its final price (including VAT),
     * and the quantity purchased.
     *
     * @param itemInformation the DTO containing static information about the item,
     *                        such as its name, description, net price, and VAT rate.
     * @param quantity        the number of items of this type that are being purchased.
     */
    public SaleItem(ItemDTO itemInformation, int quantity) {
        this.itemInformation = itemInformation;
        finalPrice.increaseAmountByPercentage(itemInformation.vatRate());
        finalPrice = new Amount(itemInformation.baseNetPrice().amount());
        this.quantity = quantity;
    }

    /**
     * Retrieves the unique identifier for this sale item.
     *
     * @return an ItemIdentifierDTO representing the identifier of the item.
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
     * @return an AmountDTO object representing the net price of the item.
     */
    public AmountDTO getBaseNetPrice() {
        return itemInformation.baseNetPrice();
    }

    /**
     * Retrieves the VAT rate associated with this sale item.
     *
     * @return a PercentageDTO representing the VAT rate of the item.
     */
    public PercentageDTO getVatRate() {
        return itemInformation.vatRate();
    }

    /**
     * Retrieves the final price of this sale item. The final price includes VAT
     * and any potential adjustments applied during the sale (e.g., by discounts).
     *
     * @return an Amount object representing the final price of the item.
     */
    public Amount getFinalPrice() {
        return finalPrice;
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
    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
