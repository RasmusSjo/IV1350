package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import main.se.kth.iv1350.rassjo.pos.model.Amount;
import main.se.kth.iv1350.rassjo.pos.model.Sale;
import main.se.kth.iv1350.rassjo.pos.model.SaleItem;

import java.util.ArrayList;
import java.util.List;

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
        List<SaleItemDTO> items = new ArrayList<>();
        for (SaleItem item : sale.getItems()) {
            items.add(toDTO(item));
        }

        return new SaleDTO(
                sale.getStartTime(),
                sale.getTotalCost(),
                sale.getTotalVAT(),
                items,
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
                item.getUnitPrice(),
                item.getVATRate(),
                toDTO(item.getFinalPrice()),
                item.getQuantity());
    }
}
