package se.kth.iv1350.rassjo.pos;

import se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.CustomerIdentifierDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestUtils {

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    public static final ItemIdentifierDTO FIRST_ITEM_ID = new ItemIdentifierDTO(10001);
    public static final ItemIdentifierDTO SECOND_ITEM_ID = new ItemIdentifierDTO(10002);

    public static final CustomerIdentifierDTO CUSTOMER_ID = new CustomerIdentifierDTO(1);

    public static BigDecimal calculateTotalGrossPrice(AmountDTO price, PercentageDTO vatRate, int quantity) {
        BigDecimal priceBigDecimal = new BigDecimal(price.amount());
        double vatRatePercentage = vatRate.percentage() / 100.0;
        BigDecimal vatRateBigDecimal = new BigDecimal(1 + vatRatePercentage);
        BigDecimal quanitytBigDecimal = new BigDecimal(quantity);

        return priceBigDecimal
                .multiply(vatRateBigDecimal)
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(quanitytBigDecimal)
                .setScale(2, RoundingMode.HALF_UP);
    }

}
