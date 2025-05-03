package main.se.kth.iv1350.rassjo.pos.integration;

public class HandlerFactory {

    private final InventoryHandler inventoryHandler;
    private final AccountingHandler accountingHandler;
    private final DiscountHandler discountHandler;

    public HandlerFactory() {
        inventoryHandler = new InventoryHandler();
        accountingHandler = new AccountingHandler();
        discountHandler = new DiscountHandler();
    }

    public InventoryHandler getInventoryHandler() {
        return null;
    }

    public AccountingHandler getAccountingHandler() {
        return null;
    }

    public DiscountHandler getDiscountHandler() {
        return null;
    }
}
