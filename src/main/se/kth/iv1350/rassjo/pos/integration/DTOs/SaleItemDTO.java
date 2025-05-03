package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

public record SaleItemDTO(ItemDTO itemInformation, AmountDTO finalPrice, int quantity) {
}
