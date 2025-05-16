package se.kth.iv1350.rassjo.pos.integration.DTOs;


import se.kth.iv1350.rassjo.pos.model.Amount;
import se.kth.iv1350.rassjo.pos.model.CashPayment;
import se.kth.iv1350.rassjo.pos.model.Sale;
import se.kth.iv1350.rassjo.pos.model.SaleItem;

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
     * Converts the provided {@code AmountDTO} object to an {@code Amount}.
     *
     * @param amount the DTO containing the monetary value to convert.
     * @return the {@code Amount} representing the monetary value.
     */
    public static Amount toDomain(AmountDTO amount) {
        return new Amount(amount.amount());
    }

    public static CashPaymentDTO toDTO(CashPayment payment){
        return new CashPaymentDTO(
                toDTO(payment.getTotalCost()),
                toDTO(payment.getPaidAmount()),
                toDTO(payment.getChange()));
    }

    /**
     * Converts the provided {@link Sale} object to a {@link SaleDTO}.
     *
     * @param sale the sale to convert
     * @return the {@link SaleDTO} representing the sale
     */
    public static SaleDTO toDTO(Sale sale) {
        return new SaleDTO(
                sale.getSaleId(),
                sale.getStartTime(),
                toDTO(sale.getTotalCost()),
                toDTO(sale.getTotalVat()),
                toDTO(sale.getItems()),
                toDTO(sale.getLastAddedItem()),
                sale.getPayment(),
                sale.getStatus());
    }

    /**
     * Converts the provided list of {@link SaleItem} objects into a list of {@link SaleItemDTO} objects.
     *
     * @param items the list of {@link SaleItem} objects to convert.
     * @return a list of {@link SaleItemDTO} objects representing the converted sale items.
     */
    public static List<SaleItemDTO> toDTO(List<SaleItem> items){
        List<SaleItemDTO> dtos = new ArrayList<>();
        for(SaleItem item : items){
            dtos.add(toDTO(item));
        }
        return dtos;
    }

    /**
     * Converts the provided {@link SaleItem} object to a {@link SaleItemDTO}.
     *
     * @param item the {@link SaleItem} object to convert.
     * @return the {@link SaleItemDTO} representing the sale item.
     */
    public static SaleItemDTO toDTO(SaleItem item) {
        return new SaleItemDTO(
                item.getId(),
                item.getName(),item.getDescription(),
                item.getBaseNetPrice(),
                item.getVatRate(),
                toDTO(item.getFinalUnitPrice()),
                toDTO(item.getFinalTotalPrice()),
                item.getQuantity());
    }
}
