package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

public record SaleItemDTO(ItemIdentifierDTO id, String name, String description, AmountDTO unitPrice,
                          PercentageDTO VATRate, AmountDTO finalPrice, int quantity) {
}
