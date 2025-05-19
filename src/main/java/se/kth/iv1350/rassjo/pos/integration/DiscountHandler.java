package se.kth.iv1350.rassjo.pos.integration;

import se.kth.iv1350.rassjo.pos.integration.DTOs.DiscountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.DiscountRequestDTO;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ServiceUnavailableException;

/**
 * The {@code DiscountHandler} class is responsible for managing discounts requests
 * in the POS system by interacting with an external discount database.
 * <p/>
 * This class is just for simulation and doesn't contain any functionality.
 */
public class DiscountHandler {

    /**
     * Creates a new instance of a {@link DiscountHandler}.
     */
    DiscountHandler() {
    }

    /**
     * Retrieves applicable discounts for a sale based on the provided discount request.
     *
     * @param discountRequest the {@link DiscountRequestDTO} containing information
     *                        about the customer and sale.
     * @return a {@link DiscountDTO} containing the calculated discount information.
     * @throws ServiceUnavailableException if the discount database is unavailable.
     */
    public DiscountDTO getDiscount(DiscountRequestDTO discountRequest) throws ServiceUnavailableException {
        throw new ServiceUnavailableException("Discount database is unavailable.");
    }
}
