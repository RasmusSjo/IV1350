package se.kth.iv1350.rassjo.pos.application;

import se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import se.kth.iv1350.rassjo.pos.integration.DTOs.CashPaymentDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for observing revenue-related updates and
 * logging total sales revenue to a file.
 */
public class TotalRevenueFileOutput implements RevenueObserver {

    private static final String BASE_PATH = "logs/";
    private static final String FILE_NAME = "revenue.log";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FORMAT = "[%s] NEW_REVENUE: totalRevenue=%.2f | saleCost=%.2f | paid=%.2f | change=%.2f%n";
    private final PrintWriter printer;
    private BigDecimal totalRevenue;

    /**
     * Initializes a new instance of the {@link TotalRevenueFileOutput} class.
     *
     * @throws RuntimeException if an I/O error occurs when setting up the file or directories.
     */
    public TotalRevenueFileOutput() {
        String revenueFilePath = BASE_PATH + FILE_NAME;

        try {
            Files.createDirectories(Path.of(BASE_PATH));
            printer = new PrintWriter(new FileWriter(revenueFilePath), true);
        } catch (IOException e) {
            throw new UncheckedIOException("An error occurred when initialising the TotalRevenueFileOutput", e);
        }

        totalRevenue = BigDecimal.ZERO;
    }

    @Override
    public void paymentReceived(CashPaymentDTO payment) {
        updateTotalRevenue(payment.totalCost());
        printTotalRevenue(payment);
    }

    private void printTotalRevenue(CashPaymentDTO payment) {
        BigDecimal totalCost = new BigDecimal(payment.totalCost().amount());
        BigDecimal paidAmount = new BigDecimal(payment.paidAmount().amount());
        BigDecimal change = new BigDecimal(payment.change().amount());

        printer.printf(LOG_FORMAT, getFormattedTime(), totalRevenue, totalCost, paidAmount, change);
    }

    private String getFormattedTime() {
        return TIME_FORMAT.format(LocalDateTime.now());
    }

    private void updateTotalRevenue(AmountDTO paidAmount) {
        totalRevenue = totalRevenue.add(new BigDecimal(paidAmount.amount()));
    }
}
