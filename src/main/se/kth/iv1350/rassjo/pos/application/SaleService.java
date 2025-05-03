package main.se.kth.iv1350.rassjo.pos.application;

import main.se.kth.iv1350.rassjo.pos.integration.AccountingHandler;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.CustomerIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DiscountHandler;
import main.se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import main.se.kth.iv1350.rassjo.pos.integration.InventoryHandler;
import main.se.kth.iv1350.rassjo.pos.model.Sale;

public class SaleService {

    private final PaymentService paymentService;
    private final InventoryHandler inventoryHandler;
    private final AccountingHandler accountingHandler;
    private final DiscountHandler discountHandler;
    private final Sale currentSale;

    public SaleService(HandlerFactory handlerFactory) {
        paymentService = new PaymentService();
        inventoryHandler = handlerFactory.getInventoryHandler();
        accountingHandler = handlerFactory.getAccountingHandler();
        discountHandler = handlerFactory.getDiscountHandler();
        currentSale = null;
    }

    public void startSale() {

    }

    public AmountDTO endSale() {
        return null;
    }

    public SaleDTO addItem(ItemIdentifierDTO itemId, int quantity) {
        return null;
    }

    public AmountDTO processCashPayment(AmountDTO amount) {
        return null;
    }

    public AmountDTO applyDiscount(CustomerIdentifierDTO customerId) {
        return null;
    }
}
