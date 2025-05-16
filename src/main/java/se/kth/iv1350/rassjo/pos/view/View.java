package se.kth.iv1350.rassjo.pos.view;

import se.kth.iv1350.rassjo.pos.application.exceptions.OperationFailedException;
import se.kth.iv1350.rassjo.pos.controller.SaleController;
import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ItemNotFoundException;
import se.kth.iv1350.rassjo.pos.utils.logging.FileLogger;

/**
 * Is an abstraction of the application view, serving as the interface between
 * the user and the system.
 */
public class View {

    private final SaleController saleController;
    private final FileLogger logger;

    /**
     * Creates a new {@link View} instance.
     *
     * @param saleController the {@code SaleController} that handles the execution
     *                        of sale-related operations.
     */
    public View(SaleController saleController) {
        this.saleController = saleController;
        this.logger = FileLogger.getInstance();
    }

    /**
     * Demonstrates a complete sale interaction sequence, showcasing the functionality of
     * the current sale system.
     * </p>
     * The method performs the following steps:
     * <ul>
     *     <li>Starts a new sale session.</li>
     *     <li>Adds multiple items to the sale, displaying details for each addition.</li>
     *     <li>Ends the sale, displaying the final total cost including VAT.</li>
     *     <li>Processes a cash payment and displays the change to be returned.</li>
     * </ul>
     */
    public void sampleRun() {
        saleController.startSale();

        addItem(new ItemIdentifierDTO(10101), 1);

        addItem(new ItemIdentifierDTO(10004), 3);

        addItem(new ItemIdentifierDTO(10009), 1);

        addItem(new ItemIdentifierDTO(10004), 4);

        endSale();

        requestDiscount(1);

        pay(new AmountDTO("1000.0"));
    }

    private void addItem(ItemIdentifierDTO itemId, int quantity ) {
        System.out.println("Add " + quantity + " item with id " + itemId.id() + ":");

        try {
            SaleDTO sale = saleController.addItem(itemId, quantity);
            SaleItemDTO lastAddedItem = sale.lastAddedItem();

            System.out.println("Item ID: " + itemId.id());
            System.out.println("Item name: " + lastAddedItem.name());
            System.out.println("Item cost (incl. VAT): " + lastAddedItem.finalUnitPrice().toString() + " SEK");
            System.out.println("VAT: " + lastAddedItem.vatRate().percentage() + "%");
            System.out.println("Item description: " + lastAddedItem.description());

            System.out.println("Total cost (incl. VAT): " + sale.totalCost().toString() + " SEK");
            System.out.println("Total VAT: " + sale.totalVat().toString() + " SEK\n");
        } catch (ItemNotFoundException e) {
            logger.warn("Item with ID " + itemId.id() + " was not found.");
            System.out.println("No item with ID " + itemId.id() + " was found.\n");
        } catch (Exception e) {
            System.out.println("An unknown error occurred trying to add the item.");
        }
    }

    private void endSale() {
        System.out.println("End sale:");
        AmountDTO totalCost = saleController.endSale();
        System.out.println("Total cost (incl. VAT): " + totalCost.toString() + " SEK\n");
    }

    private void requestDiscount(int customerId) {
        System.out.println("Requesting discount for customer with id " + customerId + ":");
        try {
            AmountDTO totalCost = saleController.requestDiscount(new CustomerIdentifierDTO(customerId));
            System.out.println("Total cost (incl. VAT): " + totalCost.toString() + " SEK\n");
        } catch (OperationFailedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unknown error occurred trying to request the discount. Please try again later.\n");
        }
    }

    private void pay(AmountDTO amountDTO) {
        AmountDTO change = saleController.processCashPayment(amountDTO);
        System.out.println("Change to give the customer: " + change.toString() + " SEK");
    }
}
