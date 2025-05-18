package se.kth.iv1350.rassjo.pos.integration;

import se.kth.iv1350.rassjo.pos.integration.DTOs.ReceiptDTO;

/**
 * The ReceiptPrinter class is responsible for printing receipts to the console.
 * It takes a {@link ReceiptDTO} object and outputs its string representation.
 */
public class ReceiptPrinter {

    /**
     * Creates a new instance of the ReceiptPrinter. This constructor initializes the
     * {@link ReceiptPrinter}, which is responsible for formatting and printing receipt information.
     */
    ReceiptPrinter() {
    }

    /**
     * Prints the string representation of the provided {@link ReceiptDTO} object to the console.
     *
     * @param receipt the {@link ReceiptDTO} object containing sales and payment information to be printed.
     *                The receipt's {@link ReceiptDTO#toString toString} method is used for formatting the output.
     */
    public void printReceipt(ReceiptDTO receipt) {
        System.out.println(receipt.toString());
    }
}
