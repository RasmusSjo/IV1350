package main.se.kth.iv1350.rassjo.pos.application;

import main.se.kth.iv1350.rassjo.pos.integration.AccountingHandler;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import main.se.kth.iv1350.rassjo.pos.integration.DiscountHandler;
import main.se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import main.se.kth.iv1350.rassjo.pos.integration.InventoryHandler;
import main.se.kth.iv1350.rassjo.pos.model.Sale;

import java.time.LocalDateTime;

/**
 * The SaleService class provides methods for managing the lifecycle of a sale, including
 * starting a sale, adding items to the sale, applying discounts, processing payments,
 * and finalizing the sale.
 */
public class SaleService {

    private final PaymentService paymentService;
    private final InventoryHandler inventoryHandler;
    private final AccountingHandler accountingHandler;
    private final DiscountHandler discountHandler;
    private Sale currentSale;

    /**
     * Constructs an instance of the {@code SaleService} class, which is responsible
     * for managing the lifecycle of a sale.
     *
     * @param handlerFactory the {@code HandlerFactory} instance used to retrieve the
     *                       necessary handlers the service depend on.
     */
    public SaleService(HandlerFactory handlerFactory) {
        paymentService = new PaymentService();
        inventoryHandler = handlerFactory.getInventoryHandler();
        accountingHandler = handlerFactory.getAccountingHandler();
        discountHandler = handlerFactory.getDiscountHandler();
        currentSale = null;
    }

    /**
     * Initiates a new sale by creating a {@code Sale} instance with the current date and time.
     */
    public void startSale() {
        LocalDateTime startTime = LocalDateTime.now();
        currentSale = new Sale(startTime);
    }

    /**
     * Ends the current sale and returns the total cost of the sale, which
     * includes VAT and any applied discounts.
     *
     * @return an {@code AmountDTO} representing the total cost of the sale.
     */
    public AmountDTO endSale() {
        currentSale.end();
        return currentSale.getTotalCost();
    }

    /**
     * Adds an item to the current sale with a specified quantity. If the
     * item already exists in the sale, its quantity is updated.
     *
     * @param itemId the identifier of the item that will be added to the sale.
     * @param quantity the quantity of the item that is being added.
     * @return a {@code SaleDTO} representing the current state of the sale after adding the item.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) {
        if (currentSale.containsItemWithId(itemId)) {
            currentSale.increaseItemWithId(itemId, quantity);
            return Mapper.toDTO(currentSale);
        }

        ItemDTO itemInformation = inventoryHandler.getItemInformation(itemId);
        currentSale.addItem(itemInformation, quantity);

        return Mapper.toDTO(currentSale);
    }

    public AmountDTO applyDiscount(CustomerIdentifierDTO customerId) {
        // This method will not be implemented
        return null;
    }

    public AmountDTO processCashPayment(AmountDTO amount) {
        return null;
    }
}
