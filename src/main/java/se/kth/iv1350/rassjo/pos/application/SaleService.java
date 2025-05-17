package se.kth.iv1350.rassjo.pos.application;

import se.kth.iv1350.rassjo.pos.application.exceptions.OperationFailedException;
import se.kth.iv1350.rassjo.pos.integration.AccountingHandler;
import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.integration.DiscountHandler;
import se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import se.kth.iv1350.rassjo.pos.integration.InventoryHandler;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ItemNotFoundException;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ServiceUnavailableException;
import se.kth.iv1350.rassjo.pos.model.CashPayment;
import se.kth.iv1350.rassjo.pos.model.Sale;
import se.kth.iv1350.rassjo.pos.model.SaleStatus;
import se.kth.iv1350.rassjo.pos.utils.logging.FileLogger;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private final FileLogger logger;
    private Sale currentSale;

    /**
     * Constructs an instance of the {@code SaleService} class, which is responsible
     * for managing the lifecycle of a sale.
     *
     * @param handlerFactory the {@code HandlerFactory} instance used to retrieve the
     *                       necessary handlers the service depend on.
     */
    public SaleService(HandlerFactory handlerFactory) {
        paymentService = new PaymentService(handlerFactory.getReceiptPrinter());
        inventoryHandler = handlerFactory.getInventoryHandler();
        accountingHandler = handlerFactory.getAccountingHandler();
        discountHandler = handlerFactory.getDiscountHandler();
        logger = FileLogger.getInstance();
        currentSale = null;
    }

    /**
     * Initiates a new sale by creating a {@code Sale} instance with the current date and time.
     */
    public void startSale() {
        LocalDateTime startTime = LocalDateTime.now();
        currentSale = new Sale(generateSaleId(), startTime);
    }

    private String generateSaleId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Ends the current sale and returns the total cost of the sale, which
     * includes VAT and any applied discounts.
     *
     * @return an {@code AmountDTO} representing the total cost of the sale.
     */
    public AmountDTO endSale() {
        currentSale.end();
        return Mapper.toDTO(currentSale.getTotalCost());
    }


    /**
     * Cancel the current sale.
     * </p>
     * Marks the current sale as {@link SaleStatus#CANCELLED} and then removes the reference to it.
     */
    public void cancelSale() {
        currentSale.cancel();
        currentSale = null;
    }

    /**
     * Adds an item to the current sale with a specified quantity. If the
     * item already exists in the sale, its quantity is updated.
     *
     * @param itemId   the identifier of the item that will be added to the sale.
     * @param quantity the quantity of the item that is being added.
     * @return a {@code SaleDTO} representing the current state of the sale after adding the item.
     * @throws ItemNotFoundException if the item with the specified identifier doesn't exist.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) throws ItemNotFoundException {
        if (currentSale.containsItemWithId(itemId)) {
            currentSale.increaseItemWithId(itemId, quantity);
            return Mapper.toDTO(currentSale);
        }

        ItemDTO itemInformation = inventoryHandler.getItemInformation(itemId);
        currentSale.addItem(itemInformation, quantity);

        return Mapper.toDTO(currentSale);
    }

    /**
     * Applies a discount to the current sale based on the provided customer's information and the
     * current sale. If a discount is successfully applied, the total cost of the sale is updated and returned.
     *
     * @param customerId the identifier of the customer for whom the discount is being sought.
     * @return an {@link AmountDTO} representing the total cost of the sale after applying the discount.
     * @throws OperationFailedException if the discount service is unavailable and the discount cannot be applied.
     */
    public AmountDTO applyDiscount(CustomerIdentifierDTO customerId) {
        try {
            DiscountRequestDTO discountRequest = new DiscountRequestDTO(
                    customerId,
                    Mapper.toDTO(currentSale.getTotalCost()),
                    Mapper.toDTO(currentSale.getItems()));
            DiscountDTO discount = discountHandler.getDiscount(discountRequest);
            currentSale.applyDiscount(discount);
            return Mapper.toDTO(currentSale.getTotalCost());
        } catch (ServiceUnavailableException e) {
            logger.error("Discount service is unavailable.", e);
            throw new OperationFailedException("Could not apply discount at this time. Try again later.", e);
        }
    }

    /**
     * Processes a cash payment for the current sale, updates the system with payment details,
     * and adjusts the inventory and accounting systems accordingly.
     *
     * @param paidAmount an {@code AmountDTO} representing the cash amount paid by the customer.
     * @return an {@code AmountDTO} representing the change to be returned to the customer.
     */
    public AmountDTO processCashPayment(AmountDTO paidAmount) {
        CashPayment payment = new CashPayment(currentSale.getTotalCost(), Mapper.toDomain(paidAmount));
        paymentService.processPayment(currentSale, payment);
        currentSale.recordPayment(payment);

        inventoryHandler.updateInventory(Mapper.toDTO(currentSale));
        accountingHandler.recordSale(Mapper.toDTO(currentSale));

        return Mapper.toDTO(payment.getChange());
    }
}
