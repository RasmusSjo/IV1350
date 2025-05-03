package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a sale consisting of the items being purchased, the items' total
 * cost (after discounts and including VAT), the total VAT for the purchase, details
 * of the payment (when performed), and the status of the sale.
 * <p>
 * This class allows management of adding items and applying discounts.
 */
public class Sale {

	private final LocalDateTime startTime;
	private final Amount totalCost;
	private final Amount totalVat;
	private CashPayment payment;
	private final Map<ItemIdentifierDTO, SaleItem> items;
	private SaleStatus status;

	/**
	 * Creates a new instance of a sale with the specified start time.
	 * Initializes the total cost, total VAT, items, and status for the sale.
	 *
	 * @param startTime the time at which the sale was initiated.
	 */
	public Sale(LocalDateTime startTime) {
		this.startTime = startTime;
		totalCost = new Amount(0);
		totalVat = new Amount(0);
		payment = null;
		items = new HashMap<>();
		status = SaleStatus.REGISTERING;
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
	 * @return an {@code AmountDTO} representing the total cost of the sale.
	 */
	public AmountDTO getTotalCost() {
		return Mapper.toDTO(totalCost);
	}

	/**
	 * Retrieves the total VAT for the sale. The returned value represents the total
	 * calculated VAT amount for all items after discounts.
	 *
	 * @return an {@code AmountDTO} representing the total VAT for the sale.
	 */
	public AmountDTO getTotalVat() {
		return Mapper.toDTO(totalVat);
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
	 * Retrieves a list of all items in the sale, represented as {@code SaleItemDTO}.
	 * Each {@code SaleItemDTO} includes details such as the item's identifier, name,
	 * description, price, VAT rate, and quantity purchased.
	 *
	 * @return a {@code List<SaleItemDTO>} containing all items in the sale.
	 */
	public List<SaleItemDTO> getItems() {
		List<SaleItemDTO> saleItems = new ArrayList<>();
		for (SaleItem item : items.values()) {
			saleItems.add(Mapper.toDTO(item));
		}
		return saleItems;
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

		updateSaleCost(item, quantity);
	}

	private void updateSaleCost(SaleItem item, int addedQuantity) {
		Amount increasedPrice = new Amount(item.getFinalPrice().getAmount() * addedQuantity);
		Amount increasedVat = new Amount(increasedPrice.getAmount());
		increasedVat.decreaseAmountByPercentage(item.getVatRate());

		totalCost.increaseAmountBySum(increasedPrice);
		totalVat.increaseAmountBySum(increasedVat);
	}

	public void applyDiscount(DiscountDTO discount) {
		// This method is not being implemented
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
}
