package se.kth.iv1350.rassjo.pos.controller;

import org.junit.jupiter.api.*;
import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.integration.HandlerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SaleControllerTest {

    private static final int ONE = 1;
    private static final int TWO = 2;

    private static HandlerFactory handlerFactory;
    private static ItemIdentifierDTO firstItemId;
    private static ItemIdentifierDTO secondItemId;
    private static ItemDTO firstItem;
    private static ItemDTO secondItem;

    private SaleController controller;

    @BeforeAll
    static void setUpClass() {
        handlerFactory = new HandlerFactory();
        firstItemId = new ItemIdentifierDTO(10001);
        secondItemId = new ItemIdentifierDTO(10002);
        firstItem = handlerFactory.getInventoryHandler().getItemInformation(firstItemId);
        secondItem = handlerFactory.getInventoryHandler().getItemInformation(secondItemId);
    }

    @AfterAll
    static void tearDownClass() {
        handlerFactory = null;
        firstItemId = null;
        secondItemId = null;
        firstItem = null;
        secondItem = null;
    }

    @BeforeEach
    void setUp() {
        controller = new SaleController(handlerFactory);
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    void testAddItemWithoutStartingSale() {
        SaleDTO result = controller.addItem(firstItemId, ONE);

        // TODO Update this test in sem 4 with exception assertion
        assertNotNull(result, "SaleDTO should not be null when adding item without starting a sale");
    }

    @Test
    void testAddItemWithStartedSale() {
        controller.startSale();
        SaleDTO sale = controller.addItem(firstItemId);

        assertNotNull(sale, "SaleDTO should not be null after adding item");
        assertNotNull(sale.lastAddedItem(), "Last added item should not be null");
    }

    @Test
    void testAddItemCorrectItemInformation() {
        int expectedQuantity = TWO;
        BigDecimal expectedItemUnitPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), ONE);
        BigDecimal expectedItemTotalPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), expectedQuantity);

        controller.startSale();
        SaleDTO sale = controller.addItem(firstItemId, expectedQuantity);
        SaleItemDTO lastAddedItem = sale.lastAddedItem();
        BigDecimal itemFinalUnitPrice = new BigDecimal(lastAddedItem.finalUnitPrice().amount());
        BigDecimal itemFinalTotalPrice = new BigDecimal(lastAddedItem.finalTotalPrice().amount());

        assertEquals(firstItemId, lastAddedItem.id(), "The added item's ID does not match the expected ID");
        assertEquals(firstItem.name(), lastAddedItem.name(), "The added item's name does not match the expected name");
        assertEquals(firstItem.description(), lastAddedItem.description(), "The added item's description does not match the expected description");
        assertEquals(firstItem.baseNetPrice(), lastAddedItem.baseNetPrice(), "The added item's base net price does not match the expected base net price");
        assertEquals(firstItem.vatRate(), lastAddedItem.vatRate(), "The added item's VAT rate does not match the expected VAT rate");
        assertEquals(expectedItemUnitPrice, itemFinalUnitPrice, "Sale calculated item gross price does not match the expected value");
        assertEquals(expectedItemTotalPrice, itemFinalTotalPrice, "Sale calculated item total price does not match the expected value");
        assertEquals(expectedQuantity, lastAddedItem.quantity(), "The quantity of the last added item does not match the expected count");
    }

    @Test
    void testAddItemTwiceCorrectQuantity() {
        int expectedQuantity = TWO + ONE;

        controller.startSale();
        controller.addItem(firstItemId, TWO);
        controller.addItem(secondItemId, ONE);
        SaleDTO sale = controller.addItem(firstItemId, ONE);

        assertEquals(firstItemId, sale.lastAddedItem().id(), "The added item's ID does not match the expected ID");
        assertEquals(expectedQuantity, sale.lastAddedItem().quantity(), "The quantity of the last added item does not match the expected count");
    }

    @Test
    void testAddItemNegativeQuantity() {
        int expectedQuantity = -ONE;
        BigDecimal expectedItemTotalPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), expectedQuantity);

        controller.startSale();
        SaleDTO sale = controller.addItem(firstItemId, expectedQuantity);
        SaleItemDTO lastAddedItem = sale.lastAddedItem();
        BigDecimal lastAddedItemTotalPrice = new BigDecimal(lastAddedItem.finalTotalPrice().amount());

        // TODO Add assertion in sem 4 for adding negative amount of items
        assertEquals(firstItemId, sale.lastAddedItem().id(), "The added item's ID does not match the expected ID");
        assertEquals(expectedItemTotalPrice, lastAddedItemTotalPrice, "Sale calculated item total price does not match the expected value");
        assertEquals(expectedQuantity, lastAddedItem.quantity(), "The quantity of the last added item does not match the expected count");
    }

    @Test
    void testAddTwoItemCorrectTotals() {
        int quantity1 = TWO;
        int quantity2 = ONE;
        BigDecimal itemNetPrice1 = calculateTotalGrossPrice(firstItem.baseNetPrice(), new PercentageDTO(0), quantity1);
        BigDecimal itemNetPrice2 = calculateTotalGrossPrice(secondItem.baseNetPrice(), new PercentageDTO(0), quantity2);
        BigDecimal itemTotalPrice1 = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity1);
        BigDecimal itemTotalPrice2 = calculateTotalGrossPrice(secondItem.baseNetPrice(), secondItem.vatRate(), quantity2);
        BigDecimal itemVatAmount1 = itemTotalPrice1.subtract(itemNetPrice1);
        BigDecimal itemVatAmount2 = itemTotalPrice2.subtract(itemNetPrice2);
        BigDecimal expectedTotalCost = itemTotalPrice1.add(itemTotalPrice2);
        BigDecimal expectedTotalVat = itemVatAmount1.add(itemVatAmount2);

        controller.startSale();
        controller.addItem(firstItemId, quantity1);
        SaleDTO sale = controller.addItem(secondItemId, quantity2);
        BigDecimal actualTotalCost = new BigDecimal(sale.totalCost().amount());
        BigDecimal actualTotalVat = new BigDecimal(sale.totalVat().amount());

        assertEquals(expectedTotalCost, actualTotalCost, "Sale total cost does not match the expected value");
        assertEquals(expectedTotalVat, actualTotalVat, "Sale total VAT does not match the expected value");
    }


    @Test
    void testEndSaleNoItemsAdded() {
        AmountDTO expectedAmount = new AmountDTO("0.00");

        controller.startSale();
        AmountDTO result = controller.endSale();

        assertEquals(expectedAmount, result, "Return amount isn't zero zero");
    }

    @Test
    void testEndSaleAfterAddingOneItem() {
        int quantity = ONE;
        BigDecimal expectedCost = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity);

        controller.startSale();
        controller.addItem(firstItemId, quantity);
        AmountDTO actualCost = controller.endSale();
        BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());

        assertEquals(expectedCost, actualCostBigDecimal, "The total amount after adding one item is incorrect");
    }

    @Test
    void testEndSaleAfterAddingMultipleItems() {
        int quantity1 = TWO;
        int quantity2 = ONE;
        BigDecimal totalItemCost1 = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity1);
        BigDecimal totalItemCost2 = calculateTotalGrossPrice(secondItem.baseNetPrice(), secondItem.vatRate(), quantity2);
        BigDecimal expectedTotalCost = totalItemCost1.add(totalItemCost2);

        controller.startSale();
        controller.addItem(firstItemId, quantity1);
        controller.addItem(secondItemId, quantity2);
        AmountDTO actualCost = controller.endSale();
        BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());

        assertEquals(expectedTotalCost, actualCostBigDecimal, "The total amount after adding multiple items is incorrect");
    }

    @Test
    void testEndSaleWithoutStartingSale() {
        controller = new SaleController(handlerFactory); // Create a new controller without starting a sale

        AmountDTO result = controller.endSale();

        // TODO Update this test in sem 4 with exception assertion
        AmountDTO expectedAmount = new AmountDTO("0.00"); // Expect zero since no sale was started
        assertEquals(expectedAmount, result, "Return amount isn't zero when sale wasn't started");
    }

    @Test
    void testStartSaleCreatesNewSale() {
        controller.startSale();
        AmountDTO result = controller.endSale();
        assertNotNull(result, "Sale result should not be null after starting a new sale");
    }


    @Test
    void testProcessCashPaymentExactAmount() {
        int quantity = TWO;
        BigDecimal paymentAmount = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity);
        BigDecimal expectedChange = new BigDecimal("0.00");

        controller.startSale();
        controller.addItem(firstItemId, quantity);
        AmountDTO actualCost = controller.endSale();
        BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());
        AmountDTO actualChange = controller.processCashPayment(new AmountDTO(paymentAmount.toString()));
        BigDecimal actualChangeBigDecimal = new BigDecimal(actualChange.amount());

        assertEquals(paymentAmount, actualCostBigDecimal, "The total amount after adding one item is incorrect");
        assertEquals(expectedChange, actualChangeBigDecimal, "Change should be zero when the payment equals the total sale cost");
    }

    @Test
    void testProcessCashPaymentExcessAmount() {
        int quantity = TWO;
        String excessAmount = "10.00";
        BigDecimal expectedTotalSaleCost = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity);
        BigDecimal paymentAmount = expectedTotalSaleCost.add(new BigDecimal(excessAmount));
        BigDecimal expectedChange = new BigDecimal(excessAmount);

        controller.startSale();
        controller.addItem(firstItemId, quantity);
        AmountDTO actualCost = controller.endSale();
        BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());
        AmountDTO actualChange = controller.processCashPayment(new AmountDTO(paymentAmount.toString()));
        BigDecimal actualChangeBigDecimal = new BigDecimal(actualChange.amount());

        assertEquals(expectedTotalSaleCost, actualCostBigDecimal, "The total amount after adding one item is incorrect");
        assertEquals(expectedChange, actualChangeBigDecimal, "Change should be zero when the payment equals the total sale cost");
    }

    @Test
    void testProcessCashPaymentBelowTotalCost() {
        int quantity = ONE;
        String excessAmount = "-10.00";
        BigDecimal expectedTotalSaleCost = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity);
        BigDecimal paymentAmount = expectedTotalSaleCost.add(new BigDecimal(excessAmount));
        BigDecimal expectedChange = new BigDecimal(excessAmount);

        controller.startSale();
        controller.addItem(firstItemId, quantity);
        AmountDTO actualCost = controller.endSale();
        BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());
        AmountDTO actualChange = controller.processCashPayment(new AmountDTO(paymentAmount.toString()));
        BigDecimal actualChangeBigDecimal = new BigDecimal(actualChange.amount());

        // TODO Update this test in sem 4 with exception assertion
        assertEquals(expectedTotalSaleCost, actualCostBigDecimal, "The total amount after adding one item is incorrect");
        assertEquals(expectedChange, actualChangeBigDecimal, "Change should be zero when the payment equals the total sale cost");
    }

    private BigDecimal calculateTotalGrossPrice(AmountDTO price, PercentageDTO vatRate, int quantity) {
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