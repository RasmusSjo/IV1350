package main.se.kth.iv1350.rassjo.pos.integration.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public record SaleDTO(LocalDateTime startTime, AmountDTO totalPrice, AmountDTO totalVAT, List<SaleItemDTO> items) {
}
