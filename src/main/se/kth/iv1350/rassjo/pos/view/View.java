package main.se.kth.iv1350.rassjo.pos.view;

import main.se.kth.iv1350.rassjo.pos.controller.SaleController;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleItemDTO;

import java.math.BigDecimal;

public class View {

    private final SaleController saleController;

    public View(SaleController saleController) {
        this.saleController = saleController;
    }

    public void sampleRun() {
        saleController.startSale();

        addItem(new ItemIdentifierDTO(10001), 1);

        addItem(new ItemIdentifierDTO(10004), 3);

        addItem(new ItemIdentifierDTO(10009), 1);

        addItem(new ItemIdentifierDTO(10004), 4);

        endSale();

        pay(new AmountDTO(BigDecimal.valueOf(1000.0)));
    }

    private void addItem(ItemIdentifierDTO itemId, int quantity ) {
        System.out.println("Add " + quantity + " item with id " + itemId.id() + ":");

        SaleDTO sale = saleController.addItem(itemId, quantity);
        SaleItemDTO lastAddedItem = sale.lastAddedItem();

        System.out.println("Item ID: " + itemId.id());
        System.out.println("Item name: " + lastAddedItem.name());
        System.out.println("Item cost (incl. VAT): " + lastAddedItem.finalUnitPrice().toString() + " SEK");
        System.out.println("VAT: " + lastAddedItem.vatRate().percentage() + "%");
        System.out.println("Item description: " + lastAddedItem.description());

        System.out.println("Total cost (incl. VAT): " + sale.totalCost().toString() + " SEK");
        System.out.println("Total VAT: " + sale.totalVat().toString() + " SEK\n");
    }

    private void endSale() {
        System.out.println("End sale:");
        AmountDTO totalCost = saleController.endSale();
        System.out.println("Total cost (incl. VAT): " + totalCost.toString() + " SEK\n");
    }

    private void pay(AmountDTO amountDTO) {
        AmountDTO change = saleController.processCashPayment(amountDTO);
        System.out.println("Change to give the customer: " + change.toString() + " SEK");
    }
}
