package se.kth.iv1350.rassjo.pos.view;

import se.kth.iv1350.rassjo.pos.application.RevenueObserver;
import se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.CashPaymentDTO;

import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * This class is responsible for displaying the total revenue generated from all sales.
 */
public class TotalRevenueView implements RevenueObserver {

    private static final String OUTPUT_FORMAT = "Total revenue after last sale is: %.2f SEK%n";
    private final PrintWriter printer;
    private BigDecimal totalRevenue;

    /**
     * Creates an instance of the {@link TotalRevenueView} class.
     */
    public TotalRevenueView() {
        printer = new PrintWriter(System.out, true);
        totalRevenue = BigDecimal.ZERO;
    }

    @Override
    public void paymentReceived(CashPaymentDTO payment) {
        updateTotalRevenue(payment.totalCost());
        printTotalRevenue();
    }

    private void updateTotalRevenue(AmountDTO paidAmount) {
        totalRevenue = totalRevenue.add(new BigDecimal(paidAmount.amount()));
    }

    private void printTotalRevenue() {
        printer.printf(OUTPUT_FORMAT, totalRevenue);
    }
}
