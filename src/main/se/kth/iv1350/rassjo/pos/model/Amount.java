package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.PercentageDTO;

public class Amount {

    private double amount;

    public Amount(double amount) {
        this.amount = amount;
    }

    public Amount(AmountDTO amount) {
        this.amount = amount.amount();
    }

    public double getAmount() {
        return amount;
    }

    public void increaseAmountBySum(AmountDTO amount) {
        this.amount += amount.amount();
    }

    public void decreaseAmountBySum(AmountDTO amount) {
        this.amount -= amount.amount();
    }

    public void decreaseAmountByPercentage(PercentageDTO percentage) {
        this.amount -= this.amount * (1 - (double) percentage.percentage() / 100);
    }
}
