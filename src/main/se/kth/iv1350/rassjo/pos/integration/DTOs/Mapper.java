package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import main.se.kth.iv1350.rassjo.pos.model.Amount;
import main.se.kth.iv1350.rassjo.pos.model.Sale;
import main.se.kth.iv1350.rassjo.pos.model.SaleItem;

/**
 * Utility class that provides methods for converting domain objects to their DTO.
 */
public class Mapper {

    /**
     * Converts the provided {@link Amount} object to an {@link AmountDTO}.
     *
     * @param amount the amount to convert.
     * @return the DTO representing the amount.
     */
    public static AmountDTO toDTO(Amount amount) {
        return new AmountDTO(amount.getAmount());
    }

    /**
     * Converts the provided {@link Sale} object to a {@link SaleDTO}.
     *
     * @param sale the sale to convert
     * @return the DTO representing the sale
     */
    public static SaleDTO toDTO(Sale sale) {
        return new SaleDTO(
                sale.getStartTime(),
                sale.getTotalCost(),
                sale.getTotalVat(),
                sale.getItems(),
                sale.getPayment(),
                sale.getStatus());
    }

    /**
     * Converts the provided {@link SaleItem} object to a {@link SaleItemDTO}.
     *
     * @param item the sale item to convert.
     * @return the DTO representing the sale item.
     */
    public static SaleItemDTO toDTO(SaleItem item) {
        return new SaleItemDTO(
                item.getId(),
                item.getName(),item.getDescription(),
                item.getNetPrice(),
                item.getVatRate(),
                toDTO(item.getFinalPrice()),
                item.getQuantity());
    }
}
