package se.kth.iv1350.rassjo.pos.model.exceptions;

import se.kth.iv1350.rassjo.pos.model.SaleStatus;

/**
 * This exception is thrown to indicate that an operation was attempted to be executed
 * in the wrong order regarding the lifecycle of a sale.
 * </p>
 * The exception provides information about the current status of the sale and the
 * status of the operation that was attempted.
 */
public class ExecutionOrderException extends IllegalStateException {

    private final SaleStatus currentStatus;
    private final SaleStatus operationStatus;

    /**
     * Creates an instance of the {@link ExecutionOrderException} class with the current
     * sale status and the status of the operation that was attempted.
     *
     * @param currentStatus the current status of the sale when the operation was attempted.
     * @param operationStatus the status required by the operation being attempted.
     */
    public ExecutionOrderException(String message, SaleStatus currentStatus, SaleStatus operationStatus) {
        super(message);
        this.currentStatus = currentStatus;
        this.operationStatus = operationStatus;
    }

    /**
     * Retrieves the current status of the sale at the time the exception was thrown.
     *
     * @return the current sale status as an instance of {@link SaleStatus}.
     */
    public SaleStatus getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Retrieves the status that the attempted operation would set the sale to.
     *
     * @return the operation status as an instance of {@link SaleStatus}.
     */
    public SaleStatus getOperationStatus() {
        return operationStatus;
    }
}
