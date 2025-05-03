package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

public class SaleItem {

    private final ItemDTO itemInformation;
    private Amount finalPrice;
    private final int quantity;

    public SaleItem(ItemDTO itemInformation, int quantity) {
        this.itemInformation = itemInformation;
        this.quantity = quantity;
    }

    public ItemIdentifierDTO getId() {
        return null;
    }

    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public AmountDTO getUnitPrice() {
        return null;
    }

    public PercentageDTO getVATRate() {
        return null;
    }

    public Amount getFinalPrice() {
        return null;
    }

    public int getQuantity() {
        return 0;
    }

    public void increaseQuantity(int quantity) {

    }
}
