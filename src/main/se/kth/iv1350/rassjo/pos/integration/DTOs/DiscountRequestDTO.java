package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import java.util.List;

public record DiscountRequestDTO(CustomerIdentifierDTO customerId, AmountDTO totalPrice, List<SaleItemDTO> items) {
}
