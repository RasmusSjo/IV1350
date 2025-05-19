package se.kth.iv1350.rassjo.pos.model;

import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.model.exceptions.ExecutionOrderException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a sale consisting of the items being purchased, the items' total
 * cost (after discounts and including VAT), the total VAT for the purchase, details
 * of the payment (when performed), and the status of the sale.
 * <p>
 * This class allows management of adding items and applying discounts.
 */
public class Sale {

	private final String saleId;
	private final LocalDateTime startTime;
	private Amount totalCost;
	private Amount totalVat;
	private CashPayment payment;
	private final Map<ItemIdentifierDTO, SaleItem> items;
	private SaleItem lastAddedItem;
	private SaleStatus status;

	/**
	 * Builder class for creating {@link Sale} instances. Implements the Builder pattern.
	 */
	public static class Builder {
		private String saleId;
		private LocalDateTime startTime;

		/**
		 * Sets the sale ID for the builder.
		 *
		 * @param saleId the unique identifier for the sale to be associated with this builder.
		 * @return the {@link Builder} instance to allow method chaining.
		 */
		public Builder saleId(String saleId) {
			this.saleId = saleId;
			return this;
		}

		/**
		 * Sets the start time for the builder.
		 *
		 * @param startTime the start time to be set, represented as a {@code LocalDateTime}.
		 * @return the {@link Builder} instance to allow method chaining.
		 */
		public Builder startTime(LocalDateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		/**
		 * Builds and returns a {@link Sale} instance with the current state of the builder.
		 *
		 * @return a new {@link Sale} instance created based on the current builder configuration.
		 * @throws IllegalStateException if the builder lacks required fields such as saleId or startTime.
		 */
		public Sale build() {
			if (saleId == null || startTime == null) {
				throw new IllegalStateException("Sale wasn't instantiated correctly");
			}
			return new Sale(this);
		}
	}

	private Sale(Builder builder) {
		this.saleId = builder.saleId;
		this.startTime = builder.startTime;
		totalCost = new Amount();
		totalVat = new Amount();
		payment = null;
		items = new HashMap<>();
		lastAddedItem = null;
		status = SaleStatus.REGISTERING;
	}

	/**
	 * Retrieves the unique identifier associated with this sale.
	 *
	 * @return a string representing the unique identifier of the sale.
	 */
	public String getSaleId() {
		return saleId;
	}

	/**
	 * Retrieves the start time of the sale.
	 *
	 * @return an {@link LocalDateTime} object representing the time the sale was initiated.
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Retrieves the total cost of the sale. This value includes VAT and any applied discounts.
	 *
	 * @return an {@link Amount} representing the total cost of the sale.
	 */
	public Amount getTotalCost() {
		return totalCost;
	}

	/**
	 * Retrieves the total VAT for the sale. The returned value represents the total
	 * calculated VAT amount for all items after discounts.
	 *
	 * @return an {@link Amount} representing the total VAT for the sale.
	 */
	public Amount getTotalVat() {
		return totalVat;
	}

	/**
	 * Retrieves the cash payment details for the current sale.
	 *
	 * @return a {@link CashPayment} object if a payment has been
	 * recorded; otherwise {@code null}.
	 */
	public CashPayment getPayment() {
		return payment;
	}

	/**
	 * Retrieves a list of all items in the sale.
	 *
	 * @return a list of {@link SaleItem} containing all items in the sale.
	 */
	public List<SaleItem> getItems() {
        return new ArrayList<>(items.values());
	}

	/**
	 * Retrieves the last item added to the current sale.
	 *
	 * @return a {@link SaleItem} representing the last item added to the sale,
	 *         or {@code null} if no items have been added.
	 */
	public SaleItem getLastAddedItem() {
		return lastAddedItem;
	}

	/**
	 * Retrieves the current status of the sale.
	 *
	 * @return the current {@link SaleStatus} of the sale, which can be one
	 * of {@link SaleStatus#REGISTERING REGISTERING}, {@link SaleStatus#AWAITING_PAYMENT AWAITING_PAYMENT},
	 * {@link SaleStatus#PAID PAID}, or {@link SaleStatus#CANCELLED CANCELLED}.
	 */
	public SaleStatus getStatus() {
		return status;
	}

	/**
	 * Checks if the current sale contains an item with the specified identifier.
	 *
	 * @param itemId the {@link ItemIdentifierDTO} representing the identifier of the item to be checked.
	 * @return {@code true} if the item is present in the sale, {@code false} otherwise.
	 */
	public boolean containsItemWithId(ItemIdentifierDTO itemId) {
		return items.containsKey(itemId);
	}

	/**
	 * Increases the quantity of an item in the sale by a specified amount.
	 * The item is identified using its identifier.
	 *
	 * @param itemId  the {@link ItemIdentifierDTO} of the item whose quantity will be increased.
	 * @param quantity the quantity by which the item's current quantity should be increased.
	 * @throws ExecutionOrderException if the sale's current status isn't {@link SaleStatus#REGISTERING REGISTERING}.
	 */
	public void increaseItemWithId(ItemIdentifierDTO itemId, int quantity) {
		if (status != SaleStatus.REGISTERING) {
			String errorMsg = "You can't increase the quantity of an item when the sale's status is " + status.toString() + ".";
			throw new ExecutionOrderException(errorMsg, status, SaleStatus.REGISTERING);
		}

		items.get(itemId).increaseQuantity(quantity);

		updateSaleCost(items.get(itemId), quantity);
		lastAddedItem = items.get(itemId);
	}

	/**
	 * Adds an item to the current sale along with its specified quantity.
	 *
	 * @param itemInformation the information of the item being added, including its
	 *                        identifier, name, description, net price, and VAT rate.
	 * @param quantity        the quantity of the item being added to the sale.
	 * @throws ExecutionOrderException if the sale's current status isn't {@link SaleStatus#REGISTERING REGISTERING}.
	 */
	public void addItem(ItemDTO itemInformation, int quantity) {
		if (status != SaleStatus.REGISTERING) {
			String errorMsg = "You can't add items to the sale when it status is " + status.toString() + ".";
			throw new ExecutionOrderException(errorMsg, status, SaleStatus.REGISTERING);
		}

		SaleItem item = new SaleItem(itemInformation, quantity);
		items.put(item.getId(), item);
		lastAddedItem = item;

		updateSaleCost(item, quantity);
	}

	private void updateSaleCost(SaleItem item, int addedQuantity) {
		Amount addedNetCost = new Amount(item.getBaseNetPrice().amount()).multiplyByQuantity(addedQuantity);
		Amount addedGrossAmount = item.getFinalUnitPrice().multiplyByQuantity(addedQuantity);
		Amount addedVatAmount = addedGrossAmount.subtract(addedNetCost);

		totalCost = totalCost.add(addedGrossAmount);
		totalVat = totalVat.add(addedVatAmount);
	}

	/**
	 * Applies a discount to the current sale given the specified discount details.
	 *
	 * @param discount the {@link DiscountDTO} object containing information about the discount to be applied.
	 */
	public void applyDiscount(DiscountDTO discount) {
		// This method is not being implemented since discounts will never be applied
	}

	/**
	 * Marks the end of the sale. Updates the sale's status to {@link SaleStatus#AWAITING_PAYMENT AWAITING_PAYMENT},
	 * indicating that all items have been registered, and the sale is now awaiting payment.
	 *
	 * @throws ExecutionOrderException if the sale's current status isn't {@link SaleStatus#REGISTERING REGISTERING}.
	 */
	public void end() {
		if (status != SaleStatus.REGISTERING) {
			String errorMsg = "You can't end the sale when it status is " + status.toString() + ".";
			throw new ExecutionOrderException(errorMsg, status, SaleStatus.REGISTERING);
		}
		status = SaleStatus.AWAITING_PAYMENT;
	}


	/**
	 * Cancels the current sale by setting its status to {@link SaleStatus#CANCELLED CANCELLED}.
	 * This operation marks the sale as terminated and can only be set before the sale has been paid.
	 *
	 * @throws ExecutionOrderException if the sale's current status either {@link SaleStatus#PAID PAID}
	 * or {@link SaleStatus#CANCELLED CANCELLED}.
	 */
	public void cancel() {
		if (status == SaleStatus.PAID || status == SaleStatus.CANCELLED) {
			String errorMsg = "You can't cancel the sale when it has already been " + (status == SaleStatus.PAID ? "paid" : "cancelled") + ".";
			throw new ExecutionOrderException(errorMsg, status, SaleStatus.CANCELLED);
		}
		status = SaleStatus.CANCELLED;
	}

	/**
	 * Record a payment for the current sale. Updates the sale's status to
	 * {@link SaleStatus#PAID PAID} and stores the provided payment details.
	 *
	 * @param payment the {@link CashPayment} object containing details of the completed payment.
	 * @throws ExecutionOrderException if the sale's current status isn't {@link SaleStatus#AWAITING_PAYMENT AWAITING_PAYMENT}.
	 */
	public void recordPayment(CashPayment payment) {
		ensureAwaitingPayment();
		this.payment = payment;
		status = SaleStatus.PAID;
	}

	/**
	 * Ensures that the status of the sale is {@link SaleStatus#AWAITING_PAYMENT AWAITING_PAYMENT}.
	 *
	 * @throws ExecutionOrderException if the current sale status is not
	 * {@link SaleStatus#AWAITING_PAYMENT AWAITING_PAYMENT}.
	 */
	public void ensureAwaitingPayment() {
		if (status != SaleStatus.AWAITING_PAYMENT) {
			String errorMsg = "The sale status must be " + SaleStatus.AWAITING_PAYMENT +  " in order to perform this operation. Current status is " + status.toString() + ".";
			throw new ExecutionOrderException(errorMsg, status, SaleStatus.AWAITING_PAYMENT);
		}
	}

	/**
	 * Two {@link Sale}s are equal if their identifiers are equal.
	 *
	 * @param o the object to compare with this sale for equality.
	 * @return {@code true} if the specified object has the same identifier as
	 * 			this object; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Sale sale = (Sale) o;
		return Objects.equals(saleId, sale.saleId);
	}

	/**
	 * Returns the hash code for the {@link Sale} object.
	 *
	 * @return an integer representing the hash code of the {@link Sale} object.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(saleId);
	}
}
