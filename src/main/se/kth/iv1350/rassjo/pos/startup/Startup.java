package main.se.kth.iv1350.rassjo.pos.startup;

import main.se.kth.iv1350.rassjo.pos.controller.SaleController;
import main.se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import main.se.kth.iv1350.rassjo.pos.view.View;

public class Startup {

    public void main(String[] args) {
        HandlerFactory handlerFactory = new HandlerFactory();
        SaleController saleController = new SaleController(handlerFactory);
        View view = new View(saleController);
    }
}
