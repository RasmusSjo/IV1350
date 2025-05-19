package se.kth.iv1350.rassjo.pos.application;

import se.kth.iv1350.rassjo.pos.application.exceptions.OperationFailedException;
import se.kth.iv1350.rassjo.pos.application.exceptions.UncheckedOperationFailedException;
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
import se.kth.iv1350.rassjo.pos.model.exceptions.ExecutionOrderException;
import se.kth.iv1350.rassjo.pos.utils.logging.FileLogger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final List<RevenueObserver> observers;
    private Sale currentSale;

    /**
     * Constructs an instance of the {@link SaleService} class, which is responsible
     * for managing the lifecycle of a sale.
     *
     * @param handlerFactory the {@link HandlerFactory} instance used to retrieve the
     *                       necessary handlers the service depend on.
     */
    public SaleService(HandlerFactory handlerFactory) {
        observers = new ArrayList<>();
        paymentService = new PaymentService(handlerFactory.getReceiptPrinter());
        inventoryHandler = handlerFactory.getInventoryHandler();
        accountingHandler = handlerFactory.getAccountingHandler();
        discountHandler = handlerFactory.getDiscountHandler();
        logger = FileLogger.getInstance();
        currentSale = null;
        logger.info("New SaleService instance created.");
    }

    /**
     * Retrieves the currently active sale in the system.
     *
     * @return a {@link SaleDTO} representing the current active sale, or null if no sale is active.
     */
    public SaleDTO getCurrentSale() {
        return currentSale == null ? null : Mapper.toDTO(currentSale);
    }

    /**
     * Initiates a new sale by creating a {@link Sale} instance with the current date and time
     * and generating a unique id for it.
     *
     * @throws UncheckedOperationFailedException if there already is an active sale.
     */
    public void startSale() {
        if (currentSale != null) {
            handleExecutionOrderException(null, "Starting of sale");
        }
        LocalDateTime startTime = LocalDateTime.now();
        currentSale = new Sale(generateSaleId(), startTime);
        logger.info("New sale started.");
    }

    private String generateSaleId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Ends the current sale and returns the total cost of the sale, which
     * includes VAT and any applied discounts.
     *
     * @return an {@link AmountDTO} representing the total cost of the sale.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the sale couldn't
     * be ended due to an invalid order of operations.
     */
    public AmountDTO endSale() {
        ensureActiveSale();
        try {
            currentSale.end();
        } catch (ExecutionOrderException e) {
            handleExecutionOrderException(e, "Ending of sale");
        }
        logger.info("Sale ended.");
        return Mapper.toDTO(currentSale.getTotalCost());
    }

    /**
     * Cancel the current sale.
     * </p>
     * Marks the current sale as {@link SaleStatus#CANCELLED CANCELLED} and then removes the reference to it.
     *
     * @throws UncheckedOperationFailedException if there is no active sale, or if the sale couldn't
     * be cancelled due to an invalid order of operations.
     */
    public void cancelSale() {
        ensureActiveSale();
        try {
            currentSale.cancel();
        } catch (ExecutionOrderException e) {
            handleExecutionOrderException(e, "Sale cancellation");
        }
        currentSale = null;
        logger.info("Sale cancelled.");
    }

    /**
     * Adds an item to the current sale with a specified quantity. If the
     * item already exists in the sale, its quantity is updated.
     *
     * @param itemId   the identifier of the item that will be added to the sale.
     * @param quantity the quantity of the item that is being added.
     * @return a {@link SaleDTO} representing the current state of the sale after adding the item.
     * @throws ItemNotFoundException if the item with the specified identifier doesn't exist.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the item couldn't
     * be added due to an invalid order of operations.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) throws ItemNotFoundException {
        ensureActiveSale();
        try {
            if (currentSale.containsItemWithId(itemId)) {
                currentSale.increaseItemWithId(itemId, quantity);
                logger.info("Quantity of item with id " + itemId.id() + " increased by " + quantity + " units.");
            }
            else {
                ItemDTO itemInformation = inventoryHandler.getItemInformation(itemId);
                currentSale.addItem(itemInformation, quantity);
                logger.info("Item with id " + itemId.id() + " added to sale.");
            }
        } catch (ExecutionOrderException e) {
            handleExecutionOrderException(e, "Addition of item");
        }
        return Mapper.toDTO(currentSale);
    }

    /**
     * Applies a discount to the current sale based on the provided customer's information and the
     * current sale. If a discount is successfully applied, the total cost of the sale is updated and returned.
     *
     * @param customerId the identifier of the customer for whom the discount is being sought.
     * @return an {@link AmountDTO} representing the total cost of the sale after applying the discount.
     * @throws OperationFailedException if the discount service is unavailable.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the sale isn't in the
     * {@link SaleStatus#AWAITING_PAYMENT AWAITING_PAYMENT} state
     */
    public AmountDTO applyDiscount(CustomerIdentifierDTO customerId) throws OperationFailedException {
        ensureActiveSale();
        try {
            currentSale.ensureAwaitingPayment();
            DiscountRequestDTO discountRequest = createDiscountRequest(customerId);
            DiscountDTO discount = discountHandler.getDiscount(discountRequest);
            currentSale.applyDiscount(discount);
        } catch (ExecutionOrderException e) {
            handleExecutionOrderException(e, "Application of discount");
        } catch (ServiceUnavailableException e) {
            logger.error("Discount service is unavailable.", e);
            throw new OperationFailedException("Could not apply discount at this time. Try again later.", e);
        }
        logger.info("Discount applied to sale.");
        return Mapper.toDTO(currentSale.getTotalCost());
    }

    private DiscountRequestDTO createDiscountRequest(CustomerIdentifierDTO customerId) {
        return new DiscountRequestDTO(
                customerId,
                Mapper.toDTO(currentSale.getTotalCost()),
                Mapper.toDTO(currentSale.getItems()));
    }

    /**
     * Processes a cash payment for the current sale, updates the system with payment details,
     * and adjusts the inventory and accounting systems accordingly.
     *
     * @param paidAmount an {@link AmountDTO} representing the cash amount paid by the customer.
     * @return an {@link AmountDTO} representing the change to be returned to the customer.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the payment couldn't
     * be processed due to an invalid order of operations.
     */
    public AmountDTO processCashPayment(AmountDTO paidAmount) {
        ensureActiveSale();
        try {
            currentSale.ensureAwaitingPayment();
        } catch (ExecutionOrderException e) {
            handleExecutionOrderException(e, "Payment");
        }
        CashPayment payment = new CashPayment(currentSale.getTotalCost(), Mapper.toDomain(paidAmount));
        paymentService.processPayment(currentSale, payment);

        // Since the AWAITING_PAYMENT state has been ensured, we don't need a try-catch here
        currentSale.recordPayment(payment);

        inventoryHandler.updateInventory(Mapper.toDTO(currentSale));
        accountingHandler.recordSale(Mapper.toDTO(currentSale));

        currentSale = null;

        notifyObservers(payment);

        logger.info("Payment processed for sale.");

        return Mapper.toDTO(payment.getChange());
    }

    /**
     * Registers an observer that will be notified when a payment is received.
     *
     * @param observer the observer to be registered, which will be notified
     *                 of revenue updates.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(CashPayment payment) {
        for (RevenueObserver observer : observers) {
            observer.paymentReceived(Mapper.toDTO(payment));
        }
    }

    private void ensureActiveSale() {
        if (currentSale == null) {
            String errorMsg = "The attempted operation can't be performed when there isn't an active sale in progress.";
            logger.error(errorMsg);
            throw new UncheckedOperationFailedException(errorMsg);
        }
    }

    private void handleExecutionOrderException(IllegalStateException e, String operationName) {
        String errorMsg = operationName + " couldn't be performed.";
        logger.error(errorMsg, e);
        throw new UncheckedOperationFailedException(errorMsg, e);
    }
}