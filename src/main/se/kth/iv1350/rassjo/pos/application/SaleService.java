package main.se.kth.iv1350.rassjo.pos.application;

import main.se.kth.iv1350.rassjo.pos.integration.AccountingHandler;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.CustomerIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
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

    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) {
        return null;
    }

    public AmountDTO processCashPayment(AmountDTO amount) {
        return null;
    }

    public AmountDTO applyDiscount(CustomerIdentifierDTO customerId) {
        return null;
    }
}
