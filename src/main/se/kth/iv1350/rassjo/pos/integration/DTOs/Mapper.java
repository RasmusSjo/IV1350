package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import main.se.kth.iv1350.rassjo.pos.model.Amount;
import main.se.kth.iv1350.rassjo.pos.model.Sale;
import main.se.kth.iv1350.rassjo.pos.model.SaleItem;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static AmountDTO toDTO(Amount amount) {
        return new AmountDTO(amount.getAmount());
    }

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
