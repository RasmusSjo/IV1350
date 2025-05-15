package se.kth.iv1350.rassjo.pos.controller;

import se.kth.iv1350.rassjo.pos.application.SaleService;
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
     * Constructs an instance of the {@code SaleController} class. This controller acts
     * as the intermediary between the presentation layer and the business logic.
     *
     * @param handlerFactory the {@code HandlerFactory} used to get the handlers
     *                       required by the service.
     */
    public SaleController(HandlerFactory handlerFactory) {
        saleService = new SaleService(handlerFactory);
    }

    /**
     * Initiates a new sale operation session.
     */
    public void startSale() {
        saleService.startSale();
    }

    /**
     * Ends the current sale and retrieves the total cost of the sale (including VAT).
     *
     * @return an {@code AmountDTO} representing the total cost of the sale (including VAT).
     */
    public AmountDTO endSale() {

        return saleService.endSale();
    }

    /**
     * Adds one unit of the item with the specified identifier to the current sale.
     *
     * @param itemId the identifier of the item to be added to the sale.
     * @return a {@code SaleDTO} representing the updated state of the sale after the item is added.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId) throws ItemNotFoundException {
        return saleService.addItem(itemId, 1);
    }

    /**
     * Adds the given quantity of the item with the specified identifier to the current sale.
     *
     * @param itemId the {@code ItemIdentifierDTO} the identifier of the item to be added to the sale.
     * @param quantity the quantity of the item to be added to the sale.
     * @return a {@code SaleDTO} representing the updated state of the sale after adding the item.
     */
    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) throws ItemNotFoundException {
        return saleService.addItem(itemId, quantity);
    }

    public AmountDTO requestDiscount(CustomerIdentifierDTO customerId) {
        // This method will not be implemented
        return null;
    }

    /**
     * Processes a cash payment for the current sale. Delegates the task to the business
     * logic layer to handle payment processing, inventory updates, and accounting adjustments.
     *
     * @param paidAmount an {@code AmountDTO} representing the cash amount paid by the customer.
     * @return an {@code AmountDTO} representing the change to be returned to the customer.
     */
    public AmountDTO processCashPayment(AmountDTO paidAmount) {
        return saleService.processCashPayment(paidAmount);
    }
}
