package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;

public class CashPayment {

    private AmountDTO totalCost;
    private Amount paidAmount;
    private Amount change;

    public CashPayment(AmountDTO totalCost, AmountDTO paidAmount) {
        this.totalCost = totalCost;
        this.paidAmount = new Amount(paidAmount);
    }

    public AmountDTO getTotalCost() {
        return null;
    }

    public AmountDTO getPaidAmount() {
        return null;
    }

    public AmountDTO getChange() {
        return null;
    }
}
