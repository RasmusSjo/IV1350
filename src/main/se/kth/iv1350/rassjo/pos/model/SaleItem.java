package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

public class SaleItem {

    private final ItemDTO itemInformation;
    private final Amount finalPrice;
    private int quantity;

    public SaleItem(ItemDTO itemInformation, int quantity) {
        this.itemInformation = itemInformation;
        finalPrice = new Amount(itemInformation.netPrice().amount());
        finalPrice.increaseAmountByPercentage(itemInformation.vatRate());
        this.quantity = quantity;
    }

    public ItemIdentifierDTO getId() {
        return itemInformation.itemId();
    }

    public String getName() {
        return itemInformation.name();
    }

    public String getDescription() {
        return itemInformation.description();
    }

    public AmountDTO getNetPrice() {
        return itemInformation.netPrice();
    }

    public PercentageDTO getVatRate() {
        return itemInformation.vatRate();
    }

    public Amount getFinalPrice() {
        return finalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}
