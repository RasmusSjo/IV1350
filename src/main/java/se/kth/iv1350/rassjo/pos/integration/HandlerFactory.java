package se.kth.iv1350.rassjo.pos.integration;

/**
 * Factory class responsible for creating and providing access to various handler instances
 * used in the POS system. These handlers encapsulate the logic for interacting with
 * external systems.
 */
public class HandlerFactory {

    private final InventoryHandler inventoryHandler;
    private final AccountingHandler accountingHandler;
    private final DiscountHandler discountHandler;
    private final ReceiptPrinter receiptPrinter;

    /**
     * Initializes a new {@link HandlerFactory} instance.
     */
    public HandlerFactory() {
        inventoryHandler = new InventoryHandler();
        accountingHandler = new AccountingHandler();
        discountHandler = new DiscountHandler();
        receiptPrinter = new ReceiptPrinter();
    }

    /**
     * Retrieves the {@link InventoryHandler} instance, which is responsible for
     * managing inventory operations.
     *
     * @return the {@code InventoryHandler} instance managed by this factory class.
     */
    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    /**
     * Retrieves the {@link AccountingHandler} instance, which is responsible for
     * handling operations related to the accounting system.
     *
     * @return the {@code AccountingHandler} instance managed by this factory class.
     */
    public AccountingHandler getAccountingHandler() {
        return accountingHandler;
    }

    /**
     * Retrieves the {@link DiscountHandler} instance, which is responsible for
     * managing discount-related operations.
     *
     * @return the {@code DiscountHandler} instance managed by this factory class.
     */
    public DiscountHandler getDiscountHandler() {
        return discountHandler;
    }

    /**
     * Retrieves the {@code ReceiptPrinter} instance, which is responsible for
     * printing receipts in the POS system.
     *
     * @return the {@code ReceiptPrinter} instance managed by this factory class.
     */
    public ReceiptPrinter getReceiptPrinter() {
        return receiptPrinter;
    }
}
