package main.se.kth.iv1350.rassjo.pos.integration;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ReceiptDTO;

/**
 * The ReceiptPrinter class is responsible for printing receipts to the console.
 * It takes a {@link ReceiptDTO} object and outputs its string representation.
 */
public class ReceiptPrinter {

    /**
     * Creates a new instance of the ReceiptPrinter.
     * This constructor initializes the ReceiptPrinter, which is responsible
     * for formatting and printing receipt information.
     */
    public ReceiptPrinter() {
    }

    /**
     * Prints the string representation of the provided {@code Receipt} object to the console.
     *
     * @param receipt the {@code Receipt} object containing sales and payment information to be printed.
     *                The receipt's {@code toString} method is used for formatting the output.
     */
    public void printReceipt(ReceiptDTO receipt) {
        System.out.println(receipt.toString());
    }
}
