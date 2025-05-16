package se.kth.iv1350.rassjo.pos.model;

import se.kth.iv1350.rassjo.pos.integration.DTOs.*;

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
	 * Initializes a new instance of the {@code Sale} class with the specified sale identifier
	 * and start time. Sets the initial state of the sale, including default values for cost,
	 * VAT, payment, and item collection.
	 *
	 * @param saleId the unique identifier for the sale.
	 * @param startTime the date and time the sale was started.
	 */
	public Sale(String saleId, LocalDateTime startTime) {
		this.saleId = saleId;
		this.startTime = startTime;
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
	 * @return a {@code String} representing the unique identifier of the sale.
	 */
	public String getSaleId() {
		return saleId;
	}

	/**
	 * Retrieves the start time of the sale.
	 *
	 * @return an {@code LocalDateTime} object representing the time the sale was initiated.
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Retrieves the total cost of the sale. This value includes VAT and any applied discounts.
	 *
	 * @return an {@code Amount} representing the total cost of the sale.
	 */
	public Amount getTotalCost() {
		return totalCost;
	}

	/**
	 * Retrieves the total VAT for the sale. The returned value represents the total
	 * calculated VAT amount for all items after discounts.
	 *
	 * @return an {@code Amount} representing the total VAT for the sale.
	 */
	public Amount getTotalVat() {
		return totalVat;
	}

	/**
	 * Retrieves the cash payment details for the current sale.
	 *
	 * @return a {@code CashPayment} object if a payment has been
	 * recorded; otherwise {@code null}.
	 */
	public CashPayment getPayment() {
		return payment;
	}

	/**
	 * Retrieves a list of all {@link SaleItem} in the sale.
	 *
	 * @return a list of {@code SaleItem} containing all items in the sale.
	 */
	public List<SaleItem> getItems() {
        return new ArrayList<>(items.values());
	}

	/**
	 * Retrieves the last item added to the current sale.
	 *
	 * @return a {@code SaleItem} representing the last item added to the sale,
	 *         or {@code null} if no items have been added.
	 */
	public SaleItem getLastAddedItem() {
		return lastAddedItem;
	}

	/**
	 * Retrieves the current status of the sale.
	 *
	 * @return the current {@code SaleStatus} of the sale, which can be one
	 * of {@code REGISTERING}, {@code AWAITING_PAYMENT}, or {@code PAID}.
	 */
	public SaleStatus getStatus() {
		return status;
	}

	/**
	 * Checks if the current sale contains an item with the specified identifier.
	 *
	 * @param itemId the {@code ItemIdentifierDTO} representing the identifier of the item to be checked.
	 * @return {@code true} if the item is present in the sale, {@code false} otherwise.
	 */
	public boolean containsItemWithId(ItemIdentifierDTO itemId) {
		return items.containsKey(itemId);
	}

	/**
	 * Increases the quantity of an item in the sale by a specified amount.
	 * The item is identified using its identifier.
	 *
	 * @param itemId  the {@code ItemIdentifierDTO} of the item whose quantity will be increased.
	 * @param quantity the amount by which the item's quantity should be increased.
	 */
	public void increaseItemWithId(ItemIdentifierDTO itemId, int quantity) {
		items.get(itemId).increaseQuantity(quantity);

		updateSaleCost(items.get(itemId), quantity);
		lastAddedItem = items.get(itemId);
	}

	/**
	 * Adds an item to the current sale along with its specified quantity.
	 * If the item is already in the sale, its unique identifier is used to manage it.
	 * The sale cost is updated to reflect the inclusion of the new item and quantity.
	 *
	 * @param itemInformation the static information of the item being added,
	 *                        including its identifier, name, description, net price, and VAT rate.
	 * @param quantity        the number of units of the item being added to the sale.
	 */
	public void addItem(ItemDTO itemInformation, int quantity) {
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
	 * Marks the end of the sale. Updates the sale's status to {@code AWAITING_PAYMENT},
	 * indicating that all items have been registered, and the sale is now awaiting payment.
	 */
	public void end() {
		status = SaleStatus.AWAITING_PAYMENT;
	}

	/**
	 * Records a payment for the current sale. Updates the sale's status to {@code PAID}
	 * and stores the provided payment details.
	 *
	 * @param payment the {@code CashPayment} object containing details of the completed payment.
	 */
	public void recordPayment(CashPayment payment) {
		this.payment = payment;
		status = SaleStatus.PAID;
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
	 * @return an integer representing the hash code of the {@code Sale} object.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(saleId);
	}
}
