package se.kth.iv1350.rassjo.pos.integration;

import org.junit.jupiter.api.Test;
import se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.CustomerIdentifierDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.DiscountRequestDTO;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ServiceUnavailableException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountHandlerTest {

    @Test
    void testGetDiscountThrowsServiceUnavailableException() {
        DiscountHandler discountHandler = new DiscountHandler();
        DiscountRequestDTO discountRequest = new DiscountRequestDTO(
                new CustomerIdentifierDTO(1),
                new AmountDTO("1.00"),
                new ArrayList<>()
        );

        assertThrows(ServiceUnavailableException.class, () -> discountHandler.getDiscount(discountRequest));
    }
}
