package main.se.kth.iv1350.rassjo.pos.model;

import main.se.kth.iv1350.rassjo.pos.integration.DTOs.AmountDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.ItemIdentifierDTO;
import main.se.kth.iv1350.rassjo.pos.integration.DTOs.DiscountDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sale {

	private final LocalDateTime startTime;
	private Amount totalCost;
	private Amount totalVAT;
	private CashPayment payment;
	private List<SaleItem> items;
	private SaleStatus status;

	public Sale(LocalDateTime startTime) {
		this.startTime = startTime;
		totalCost = new Amount(0);
		totalVAT = new Amount(0);
		items = new ArrayList<>();
		status = SaleStatus.REGISTERING;
	}

	public LocalDateTime getStartTime() {
		return null;
	}

	public AmountDTO getTotalCost() {
		return null;
	}

	public AmountDTO getTotalVAT() {
		return null;
	}

	public CashPayment getPayment() {
		return null;
	}

	public List<SaleItem> getItems() {
		return null;
	}

	public SaleStatus getStatus() {
		return null;
	}

	public boolean containsItemWithId(ItemIdentifierDTO itemId) {
		return false;
	}

	public void increaseItemWithId(ItemIdentifierDTO itemId) {

	}

	public void addItem(ItemIdentifierDTO itemId, int quantity) {

	}

	public void applyDiscount(DiscountDTO discount) {

	}

	public void end() {

	}

	public void recordPayment(CashPayment amount) {

	}

	public void cancel() {

	}
}
