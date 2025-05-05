package se.kth.iv1350.rassjo.pos.startup;

import se.kth.iv1350.rassjo.pos.controller.SaleController;
import se.kth.iv1350.rassjo.pos.integration.HandlerFactory;
import se.kth.iv1350.rassjo.pos.view.View;

public class Startup {

    public static void main(String[] args) {
        HandlerFactory handlerFactory = new HandlerFactory();
        SaleController saleController = new SaleController(handlerFactory);
        View view = new View(saleController);

        view.sampleRun();
    }
}
