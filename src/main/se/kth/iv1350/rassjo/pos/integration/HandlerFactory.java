package main.se.kth.iv1350.rassjo.pos.integration;

/**
 * Factory class responsible for creating and providing access to various handler instances
 * used in the POS system. These handlers encapsulate the logic for interacting with
 * external systems.
 */
public class HandlerFactory {

    private final InventoryHandler inventoryHandler;
    private final AccountingHandler accountingHandler;
    private final DiscountHandler discountHandler;

    /**
     * Initializes a new instance of the HandlerFactory class and creates instances of the various handlers,
     * including the InventoryHandler, AccountingHandler, and DiscountHandler.
     */
    public HandlerFactory() {
        inventoryHandler = new InventoryHandler();
        accountingHandler = new AccountingHandler();
        discountHandler = new DiscountHandler();
    }

    /**
     * Retrieves the {@code InventoryHandler} instance, which is responsible for
     * managing inventory operations.
     *
     * @return the {@code InventoryHandler} instance managed by this factory class.
     */
    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    /**
     * Retrieves the {@code AccountingHandler} instance, which is responsible for
     * handling operations related to the accounting system.
     *
     * @return the {@code AccountingHandler} instance managed by this factory class.
     */
    public AccountingHandler getAccountingHandler() {
        return accountingHandler;
    }

    /**
     * Retrieves the {@code DiscountHandler} instance, which is responsible for
     * managing discount-related operations.
     *
     * @return the {@code DiscountHandler} instance managed by this factory class.
     */
    public DiscountHandler getDiscountHandler() {
        return discountHandler;
    }
}
