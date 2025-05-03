package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

public record ItemDTO(ItemIdentifierDTO id, String name, String description, AmountDTO unitPrice,
                      PercentageDTO VATRate) {
}
