package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

public class Amount {

    private double amount;

    public Amount(double amount) {
        this.amount = amount;
    }

    public Amount(AmountDTO amount) {

    }

    public double getAmount() {
        return 0;
    }

    public void increaseAmountBySum(AmountDTO amount) {

    }

    public void decreaseAmountBySum(AmountDTO amount) {

    }

    public void decreaseAmountByPercentage(PercentageDTO percentage) {

    }
}
