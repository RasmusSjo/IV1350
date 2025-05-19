package se.kth.iv1350.rassjo.pos.controller;

import se.kth.iv1350.rassjo.pos.application.RevenueObserver;
import se.kth.iv1350.rassjo.pos.application.SaleService;
import se.kth.iv1350.rassjo.pos.application.exceptions.OperationFailedException;
import se.kth.iv1350.rassjo.pos.application.exceptions.UncheckedOperationFailedException;
import se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.CustomerIdentifierDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ItemNotFoundException;

/**
 * The SaleController class serves as the primary interface between the view
 * and business logic layer, facilitating management of sales operations.
 */
public class SaleController {

    private final SaleService saleService;

    /**
     * Constructs an instance of the {@link SaleController} class. This controller acts
     * as the intermediary between the presentation layer and the business logic.
     *
     * @param handlerFactory the {@link HandlerFactory} used to get the handlers
     *                       required by the service.
     */
    public SaleController(HandlerFactory handlerFactory) {
        saleService = new SaleService(handlerFactory);
    }

    /**
     * Initiates a new sale operation session.
     *
     * @throws UncheckedOperationFailedException if there already is an active sale in progress.
     */
    public void startSale() {
        saleService.startSale();
    }

    /**
     * Ends the current sale and retrieves the total cost of the sale (including VAT).
     *
     * @return an {@link AmountDTO} representing the total cost of the sale (including VAT).
     * @throws UncheckedOperationFailedException if there is no active sale, or if the sale couldn't be ended
     *                                  due to an invalid order of operations.
     */
    public AmountDTO endSale() {
        return saleService.endSale();
    }

    /**
     * Cancels the current sale session.
     *
     * @throws UncheckedOperationFailedException if there is no active sale, or if the sale couldn't
     *                                  be cancelled due to an invalid order of operations.
     */
    public void cancelSale() {
        saleService.cancelSale();
    }

    /**
     * Adds one unit of the item with the specified identifier to the current sale.
     *
     * @param itemId the identifier of the item to be added to the sale.
     * @return a {@link SaleDTO} representing the updated state of the sale after the item is added.
     * @throws ItemNotFoundException if the item with the specified identifier doesn't exist.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the item couldn't be
     *                                  added to the sale due to an invalid order of operations.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId) throws ItemNotFoundException {
        return saleService.addItem(itemId, 1);
    }

    /**
     * Adds the given quantity of the item with the specified identifier to the current sale.
     *
     * @param itemId the {@link ItemIdentifierDTO} the identifier of the item to be added to the sale.
     * @param quantity the quantity of the item to be added to the sale.
     * @return a {@link SaleDTO} representing the updated state of the sale after adding the item.
     * @throws ItemNotFoundException if the item with the specified identifier doesn't exist.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the item couldn't be
     *                                  added to the sale due to an invalid order of operations.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) throws ItemNotFoundException {
        return saleService.addItem(itemId, quantity);
    }

    /**
     * Requests a discount for the current sale based on the provided customer identifier and current sale.
     *
     * @param customerId the identifier of the customer for whom the discount is requested.
     * @return an {@link AmountDTO} representing the total cost of the sale after applying the discount.
     * @throws OperationFailedException if the discount cannot be applied due to a system failure.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the discount couldn't be
     * applied due to an invalid order of operations
     */
    public AmountDTO requestDiscount(CustomerIdentifierDTO customerId) throws OperationFailedException {
        return saleService.applyDiscount(customerId);
    }

    /**
     * Processes a cash payment for the current sale.
     *
     * @param paidAmount an {@link AmountDTO} representing the cash amount paid by the customer.
     * @return an {@link AmountDTO} representing the change to be returned to the customer.
     * @throws UncheckedOperationFailedException if there is no active sale, or if the payment couldn't be
     *                                  processed due to an invalid order of operations.
     */
    public AmountDTO processCashPayment(AmountDTO paidAmount) {
        return saleService.processCashPayment(paidAmount);
    }

    /**
     * Adds a revenue observer to the system.
     *
     * @param observer the {@link RevenueObserver} to be added, which will receive
     *                 notifications of revenue updates.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        saleService.addRevenueObserver(observer);
    }
}
