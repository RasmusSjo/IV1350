package main.se.kth.iv1350.rassjo.pos.controller;

import main.se.kth.iv1350.rassjo.pos.application.SaleService;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.CustomerIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.SaleDTO;
import main.se.kth.iv1350.rassjo.pos.integration.HandlerFactory;

public class SaleController {

    private final SaleService saleService;

    public SaleController(HandlerFactory handlerFactory) {
        saleService = new SaleService(handlerFactory);
    }

    public void startSale() {

    }

    public AmountDTO endSale() {
        return null;
    }

    public SaleDTO addItem(ItemIdentifierDTO itemId) {
        return null;
    }

    public SaleDTO addItem(ItemIdentifierDTO item, int quantity) {
        return null;
    }

    public AmountDTO requestDiscount(CustomerIdentifierDTO customerId) {
        return null;
    }

    public AmountDTO processCashPayment(AmountDTO paidAmount) {
        return null;
    }
}
