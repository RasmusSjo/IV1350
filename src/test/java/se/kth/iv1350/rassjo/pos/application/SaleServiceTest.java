package se.kth.iv1350.rassjo.pos.application;

import org.junit.jupiter.api.*;
import se.kth.iv1350.rassjo.pos.application.exceptions.OperationFailedException;
import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ItemNotFoundException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static se.kth.iv1350.rassjo.pos.TestUtils.*;

public class SaleServiceTest {

    private static HandlerFactory handlerFactory;
    private static SaleService saleService;
    private static ItemDTO firstItem;

    @BeforeAll
    static void setUpClass() throws ItemNotFoundException {
        handlerFactory = new HandlerFactory();
        firstItem = handlerFactory.getInventoryHandler().getItemInformation(FIRST_ITEM_ID);
    }

    @AfterAll
    static void tearDownClass() {
        handlerFactory = null;
    }

    @BeforeEach
    void setUp() {
        saleService = new SaleService(handlerFactory);
    }

    @AfterEach
    void tearDown() {
        saleService = null;
    }

    @Test
    void testStartSaleCreatesNewSale() {
        SaleDTO beforeStart = saleService.getCurrentSale();
        saleService.startSale();
        SaleDTO afterStart = saleService.getCurrentSale();

        assertNull(beforeStart, "The current sale should be null before starting a new sale.");
        assertNotNull(afterStart, "A new sale should be initiated.");
        assertNotNull(afterStart.saleId(), "The sale ID should be generated for a new sale.");
    }

    @Test
    void testStartSaleThrowsErrorIfSaleAlreadyInProgress() {
        saleService.startSale();

        OperationFailedException exception = assertThrows(OperationFailedException.class, () -> saleService.startSale(), "Starting a sale while already in progress should throw an exception.");
        assertEquals("Starting of sale couldn't be performed.", exception.getMessage(), "Exception should contain the correct error message.");
    }


    @Test
    void testEndSaleFinalizesSale() {
        saleService.startSale();
        AmountDTO totalCost = saleService.endSale();

        assertNotNull(totalCost, "Total cost should not be null after ending a sale.");
    }

    @Test
    void testEndSaleThrowsErrorIfNoActiveSale() {
        OperationFailedException exception = assertThrows(OperationFailedException.class, () -> saleService.endSale(), "Ending a sale with no active sale should throw an exception.");
        assertEquals("The attempted operation can't be performed when there isn't an active sale in progress.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testCancelSaleSetsCurrentSaleToNull() {
        saleService.startSale();
        saleService.cancelSale();
        SaleDTO afterCancel = saleService.getCurrentSale();

        assertNull(afterCancel, "The current sale should be null after cancellation.");
    }

    @Test
    void testCancelSaleThrowsErrorIfNoActiveSale() {
        OperationFailedException exception = assertThrows(OperationFailedException.class, () -> saleService.cancelSale(), "Cancelling a sale with no active sale should throw an exception.");
        assertEquals("The attempted operation can't be performed when there isn't an active sale in progress.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testAddItemSuccessfullyAddsItemToSale() {
        int expectedQuantity = TWO;

        saleService.startSale();

        SaleDTO updatedSale = assertDoesNotThrow(() -> saleService.addItem(FIRST_ITEM_ID, expectedQuantity), "Adding an item should not throw an exception when the sale is active and in REGISTERING state.");
        assertNotNull(updatedSale, "Updated sale should not be null after adding an item.");
        assertNotNull(updatedSale.lastAddedItem(), "Last added item should not be null after adding an item.");
        assertEquals(FIRST_ITEM_ID, updatedSale.lastAddedItem().id(), "The ID of the last added item should match the provided item ID.");
        assertEquals(expectedQuantity, updatedSale.lastAddedItem().quantity(), "The quantity of the last added item should match the provided quantity.");
    }

    @Test
    void testAddItemIncreasesQuantityForExistingItem() throws ItemNotFoundException {
        saleService.startSale();
        int initialQuantity = TWO;
        int additionalQuantity = THREE;

        saleService.addItem(FIRST_ITEM_ID, initialQuantity);
        SaleDTO updatedSale = saleService.addItem(FIRST_ITEM_ID, additionalQuantity);
        assertNotNull(updatedSale, "Updated sale should not be null after increasing item quantity.");
        assertEquals(initialQuantity + additionalQuantity, updatedSale.lastAddedItem().quantity(), "The total quantity should reflect the sum of the initial and additional quantities.");
    }

    @Test
    void testAddItemAddsExpectedItem() throws ItemNotFoundException {
        saleService.startSale();
        int expectedQuantity = TWO;
        BigDecimal expectedItemUnitPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), ONE);
        BigDecimal expectedItemTotalPrice = calculateTotalGrossPrice(firstItem.baseNetPrice(), firstItem.vatRate(), expectedQuantity);

        SaleDTO sale = saleService.addItem(FIRST_ITEM_ID, expectedQuantity);
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
    void testAddItemThrowsErrorIfNoActiveSale() {
        OperationFailedException exception = assertThrows(
                OperationFailedException.class,
                () -> saleService.addItem(FIRST_ITEM_ID, ONE),
                "Adding an item without an active sale should throw an exception.");
        assertEquals("The attempted operation can't be performed when there isn't an active sale in progress.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testAddItemThrowsErrorForInvalidItem() {
        saleService.startSale();
        ItemIdentifierDTO invalidItemId = new ItemIdentifierDTO(-1);

        assertThrows(ItemNotFoundException.class,
                () -> saleService.addItem(invalidItemId, ONE),
                "Adding an invalid item identifier should throw an ItemNotFoundException.");
    }

    @Test
    void testAddItemThrowsErrorWhenSaleEnded() {
        saleService.startSale();
        saleService.endSale();

        OperationFailedException exception = assertThrows(
                OperationFailedException.class,
                () -> saleService.addItem(FIRST_ITEM_ID, ONE),
                "Adding an item after the sale has been ended should throw an exception.");
        assertEquals("Addition of item couldn't be performed.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testApplyDiscountThrowsErrorIfNoActiveSale() {
        OperationFailedException exception = assertThrows(OperationFailedException.class, () -> saleService.applyDiscount(CUSTOMER_ID), "Applying a discount without an active sale should throw an exception.");
        assertEquals("The attempted operation can't be performed when there isn't an active sale in progress.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testApplyDiscountThrowsErrorIfCalledBeforeSaleFinalization() {
        saleService.startSale();

        OperationFailedException exception = assertThrows(
                OperationFailedException.class,
                () -> saleService.applyDiscount(CUSTOMER_ID),
                "Applying a discount before the sale is set to Awaiting Payment state should throw an exception.");
        assertEquals("Application of discount couldn't be performed.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testApplyDiscountThrowsErrorWhenDiscountServiceUnavailable() {
        saleService.startSale();
        saleService.endSale();

        OperationFailedException exception = assertThrows(
                OperationFailedException.class,
                () -> saleService.applyDiscount(CUSTOMER_ID),
                "Applying a discount when the discount service is unavailable should throw an exception.");
        assertEquals("Could not apply discount at this time. Try again later.", exception.getMessage(), "Exception should contain the correct error message.");
    }


    @Test
    void testProcessCashPaymentReturnsCorrectChange() {
        String expectedChange = "20.00";

        saleService.startSale();
        saleService.endSale();
        AmountDTO totalCost = saleService.getCurrentSale().totalCost();
        BigDecimal paidAmount = new BigDecimal(totalCost.amount()).add(new BigDecimal(expectedChange));

        AmountDTO actualChange = assertDoesNotThrow(
                () -> saleService.processCashPayment(new AmountDTO(paidAmount.toPlainString())),
                "Processing cash payment should not throw an exception when sale is in Awaiting Payment state.");
        assertNotNull(actualChange, "Change should not be null after processing cash payment.");
        assertEquals(expectedChange, actualChange.amount(), "The change should match the overpaid amount.");
    }

    @Test
    void testProcessCashPaymentThrowsErrorIfNoActiveSale() {
        OperationFailedException exception = assertThrows(
                OperationFailedException.class,
                () -> saleService.processCashPayment(new AmountDTO("100.00")),
                "Processing cash payment without an active sale should throw an exception.");
        assertEquals("The attempted operation can't be performed when there isn't an active sale in progress.", exception.getMessage(), "Exception should contain the correct error message.");
    }

    @Test
    void testProcessCashPaymentThrowsErrorIfSaleNotAwaitingPayment() {
        saleService.startSale();

        OperationFailedException exception = assertThrows(
                OperationFailedException.class,
                () -> saleService.processCashPayment(new AmountDTO("100.00")),
                "Processing cash payment before sale is in Awaiting Payment state should throw an exception.");
        assertEquals("Payment couldn't be performed.", exception.getMessage(), "Exception should contain the correct error message.");
    }
}
