package se.kth.iv1350.rassjo.pos.model;

/**
 * Represents the status of a sale in the POS system by defining the
 * possible states a sale can be in during its lifecycle.
 */
public enum SaleStatus {
    /**
     * Indicates that the sale is in the process of being registered.
     * <p>
     * <ul>
     *     <li>
     *         In this state, items are actively being added to the sale.
     *     </li>
     * </ul>
     */
    REGISTERING,
    /**
     * Represents the status of a sale when all items have been registered,
     * and the system is awaiting payment from the customer.
     * <p>
     * <ul>
     *     <li>
     *         In this state, no further items can be added to the sale.
     *     </li>
     * </ul>
     */
    AWAITING_PAYMENT,
    /**
     * Indicates that a sale has been fully paid, and the transaction is complete.
     * <p>
     * <ul>
     *     <li>
     *         In this state, payment has been successfully recorded, and no further
     *         actions can be performed on the sale.
     *     </li>
     * </ul>
     */
    PAID,
    /**
     * Indicates that the sale has been cancelled.
     * <p>
     * <ul>
     *     <li>
     *         This status represents a state where the sale has been terminated before being paid.
     *     </li>
     * </ul>
     */
    CANCELLED
}
