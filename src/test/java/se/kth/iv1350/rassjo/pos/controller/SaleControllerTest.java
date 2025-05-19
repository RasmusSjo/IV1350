package se.kth.iv1350.rassjo.pos.controller;

import org.junit.jupiter.api.*;
import se.kth.iv1350.rassjo.pos.application.exceptions.OperationFailedException;
import se.kth.iv1350.rassjo.pos.application.exceptions.UncheckedOperationFailedException;
import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ItemNotFoundException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static se.kth.iv1350.rassjo.pos.TestUtils.*;

class SaleControllerTest {

    private static final String NO_EXCEPTION_ADD_TO_ACTIVE_SALE =
            "An exception should not be thrown when adding an item to an active sale (started and not yet ended).";

    private static HandlerFactory handlerFactory;
    private static ItemDTO firstItem;
    private static ItemDTO secondItem;

    private SaleController controller;

    @BeforeAll
    static void setUpClass() throws ItemNotFoundException {
        handlerFactory = new HandlerFactory();
        firstItem = handlerFactory.getInventoryHandler().getItemInformation(FIRST_ITEM_ID);
        secondItem = handlerFactory.getInventoryHandler().getItemInformation(SECOND_ITEM_ID);
    }

    @AfterAll
    static void tearDownClass() {
        handlerFactory = null;
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

    @Nested
    class StartSaleTests {
        @Test
        void testStartSaleCreatesNewSale() {
            controller.startSale();
            AmountDTO result = controller.endSale();
            assertNotNull(result, "Sale result should not be null after ending a started sale");
        }
    }

    @Nested
    class CancelSaleTests {

        @Test
        void testCancelSaleDuringActiveSale() {
            controller.startSale();
            assertDoesNotThrow(() -> controller.cancelSale(),
                    "Canceling an active sale should not throw an exception");

            // Attempt to end the canceled sale and verify it behaves as if no sale was started
            assertThrows(UncheckedOperationFailedException.class,
                    () -> controller.endSale(),
                    "Ending a sale after cancellation should throw an OperationFailedException");
        }

        @Test
        void testCancelSaleWithoutStartingSale() {
            assertThrows(UncheckedOperationFailedException.class,
                    () -> controller.cancelSale(),
                    "Canceling a sale without starting one should throw an OperationFailedException");
        }
    }

    @Nested
    class EndSaleTests {

        @Test
        void testEndSaleNoItemsAdded() {
            AmountDTO expectedAmount = new AmountDTO("0.00");

            controller.startSale();
            AmountDTO result = controller.endSale();

            assertEquals(expectedAmount, result, "Return amount isn't zero zero");
        }

        @Test
        void testEndSaleAfterAddingOneItem() throws ItemNotFoundException {
            int quantity = ONE;
            BigDecimal expectedCost = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity);

            controller.startSale();
            controller.addItem(FIRST_ITEM_ID, quantity);
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
            assertDoesNotThrow(() -> controller.addItem(FIRST_ITEM_ID, quantity1));
            assertDoesNotThrow(() -> controller.addItem(SECOND_ITEM_ID, quantity2));
            AmountDTO actualCost = controller.endSale();
            BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());

            assertEquals(expectedTotalCost, actualCostBigDecimal, "The total amount after adding multiple items is incorrect");
        }

        @Test
        void testEndSaleWithoutStartingSale() {
            controller = new SaleController(handlerFactory);

            // TODO Should probably throw a different exception but not a requirement for this assignment.
            assertThrows(UncheckedOperationFailedException.class,
                    () -> controller.endSale(),
                    "Ending a sale before starting a sale should throw a NullPointerException");
        }
    }

    @Nested
    class AddItemTests {

        @Test
        void testAddItemWithoutStartingSale() {
            controller = new SaleController(handlerFactory);

            // TODO Should probably throw a different exception but not a requirement for this assignment.
            assertThrows(UncheckedOperationFailedException.class,
                    () -> controller.addItem(FIRST_ITEM_ID, ONE),
                    "Adding an item without starting a sale should throw OperationFailedException" );
        }

        @Test
        void testAddItemWithStartedSale() {
            controller = new SaleController(handlerFactory);
            controller.startSale();

            SaleDTO sale = assertDoesNotThrow(() -> controller.addItem(FIRST_ITEM_ID),
                    "An exception should not be thrown when adding an item to an active sale (started and not yet ended).");
            assertNotNull(sale, "SaleDTO should not be null after starting a sale and adding an item.");
            assertNotNull(sale.lastAddedItem(), "Last added item should not be null after adding an item.");
        }

        @Test
        void testAddItemCorrectItemInformation() throws ItemNotFoundException {
            controller.startSale();
            int expectedQuantity = TWO;
            BigDecimal expectedItemUnitPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), ONE);
            BigDecimal expectedItemTotalPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), expectedQuantity);

            SaleDTO sale = controller.addItem(FIRST_ITEM_ID, expectedQuantity);
            SaleItemDTO lastAddedItem = sale.lastAddedItem();
            BigDecimal itemFinalUnitPrice = new BigDecimal(lastAddedItem.finalUnitPrice().amount());
            BigDecimal itemFinalTotalPrice = new BigDecimal(lastAddedItem.finalTotalPrice().amount());

            assertEquals(FIRST_ITEM_ID, lastAddedItem.id(), "The added item's ID does not match the expected ID");
            assertEquals(firstItem.name(), lastAddedItem.name(), "The added item's name does not match the expected name");
            assertEquals(firstItem.description(), lastAddedItem.description(), "The added item's description does not match the expected description");
            assertEquals(firstItem.baseNetPrice(), lastAddedItem.baseNetPrice(), "The added item's base net price does not match the expected base net price");
            assertEquals(firstItem.vatRate(), lastAddedItem.vatRate(), "The added item's VAT rate does not match the expected VAT rate");
            assertEquals(expectedItemUnitPrice, itemFinalUnitPrice, "Sale calculated item gross price does not match the expected value");
            assertEquals(expectedItemTotalPrice, itemFinalTotalPrice, "Sale calculated item total price does not match the expected value");
            assertEquals(expectedQuantity, lastAddedItem.quantity(), "The quantity of the last added item does not match the expected count");
        }

        @Test
        void testAddItemTwiceCorrectQuantity() throws ItemNotFoundException {
            int expectedQuantity = TWO + ONE;

            controller.startSale();
            controller.addItem(FIRST_ITEM_ID, TWO);
            controller.addItem(SECOND_ITEM_ID, ONE);
            SaleDTO sale = controller.addItem(FIRST_ITEM_ID, ONE);

            assertEquals(FIRST_ITEM_ID, sale.lastAddedItem().id(), "The added item's ID does not match the expected ID");
            assertEquals(expectedQuantity, sale.lastAddedItem().quantity(), "The quantity of the last added item does not match the expected count");
        }

        @Test
        void testAddItemNegativeQuantity() throws ItemNotFoundException {
            int expectedQuantity = -ONE;
            BigDecimal expectedItemTotalPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), expectedQuantity);

            controller.startSale();
            SaleDTO sale = controller.addItem(FIRST_ITEM_ID, expectedQuantity);
            SaleItemDTO lastAddedItem = sale.lastAddedItem();
            BigDecimal lastAddedItemTotalPrice = new BigDecimal(lastAddedItem.finalTotalPrice().amount());

            // TODO Method should probably throw an exception but not a requirement for this assignment.
            assertEquals(FIRST_ITEM_ID, sale.lastAddedItem().id(), "The added item's ID does not match the expected ID");
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
            assertDoesNotThrow(() -> controller.addItem(FIRST_ITEM_ID, quantity1));
            SaleDTO sale = assertDoesNotThrow(() -> controller.addItem(SECOND_ITEM_ID, quantity2));
            BigDecimal actualTotalCost = new BigDecimal(sale.totalCost().amount());
            BigDecimal actualTotalVat = new BigDecimal(sale.totalVat().amount());

            assertEquals(expectedTotalCost, actualTotalCost, "Sale total cost does not match the expected value");
            assertEquals(expectedTotalVat, actualTotalVat, "Sale total VAT does not match the expected value");
        }
    }

    @Nested
    class DiscountRequestTests {

        @Test
        void testRequestDiscountThrowsErrorWhenDiscountServiceUnavailable() {
            controller.startSale();
            controller.endSale();

            assertThrows(
                    OperationFailedException.class,
                    () -> controller.requestDiscount(CUSTOMER_ID),
                    "Applying a discount when the discount service is unavailable should throw an exception.");        }
    }

    @Nested
    class ProcessCashPaymentTests {

        @Test
        void testProcessCashPaymentExactAmount() {
            int quantity = TWO;
            BigDecimal paymentAmount = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), quantity);
            BigDecimal expectedChange = new BigDecimal("0.00");

            controller.startSale();
            assertDoesNotThrow(() -> controller.addItem(FIRST_ITEM_ID, quantity));
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
            assertDoesNotThrow(() -> controller.addItem(FIRST_ITEM_ID, quantity));
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
            assertDoesNotThrow(() -> controller.addItem(FIRST_ITEM_ID, quantity));
            AmountDTO actualCost = controller.endSale();
            BigDecimal actualCostBigDecimal = new BigDecimal(actualCost.amount());
            AmountDTO actualChange = controller.processCashPayment(new AmountDTO(paymentAmount.toString()));
            BigDecimal actualChangeBigDecimal = new BigDecimal(actualChange.amount());

            // TODO Update this test in sem 4 with exception assertion
            assertEquals(expectedTotalSaleCost, actualCostBigDecimal, "The total amount after adding one item is incorrect");
            assertEquals(expectedChange, actualChangeBigDecimal, "Change should be zero when the payment equals the total sale cost");
        }
    }
}