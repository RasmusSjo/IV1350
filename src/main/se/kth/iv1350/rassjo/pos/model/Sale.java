package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sale {

	private final LocalDateTime startTime;
	private final Amount totalCost;
	private final Amount totalVat;
	private CashPayment payment;
	private final Map<ItemIdentifierDTO, SaleItem> items;
	private SaleStatus status;

	public Sale(LocalDateTime startTime) {
		this.startTime = startTime;
		totalCost = new Amount(0);
		totalVat = new Amount(0);
		payment = null;
		items = new HashMap<>();
		status = SaleStatus.REGISTERING;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public AmountDTO getTotalCost() {
		return Mapper.toDTO(totalCost);
	}

	public AmountDTO getTotalVat() {
		return Mapper.toDTO(totalVat);
	}

	public CashPayment getPayment() {
		return payment;
	}

	public List<SaleItemDTO> getItems() {
		List<SaleItemDTO> saleItems = new ArrayList<>();
		for (SaleItem item : items.values()) {
			saleItems.add(Mapper.toDTO(item));
		}
		return saleItems;
	}

	public SaleStatus getStatus() {
		return status;
	}

	public boolean containsItemWithId(ItemIdentifierDTO itemId) {
		return items.containsKey(itemId);
	}

	public void increaseItemWithId(ItemIdentifierDTO itemId, int quantity) {
		items.get(itemId).increaseQuantity(quantity);
	}

	public void addItem(ItemDTO itemInformation, int quantity) {
		SaleItem item = new SaleItem(itemInformation, quantity);
		items.put(item.getId(), item);

		updateSaleCost(item, quantity);
	}

	private void updateSaleCost(SaleItem item, int addedQuantity) {
		AmountDTO increasedPrice = new AmountDTO(item.getFinalPrice().getAmount() * addedQuantity);
		Amount increasedVat = new Amount(increasedPrice.amount());
		increasedVat.decreaseAmountByPercentage(item.getVatRate());

		totalCost.increaseAmountBySum(increasedPrice);
		totalVat.increaseAmountBySum(increasedVat);
	}

	public void applyDiscount(DiscountDTO discount) {
		// This method is not being implemented
	}

	public void end() {
		status = SaleStatus.AWAITING_PAYMENT;
	}

	public void recordPayment(CashPayment payment) {
		this.payment = payment;
		status = SaleStatus.PAID;
	}
}
