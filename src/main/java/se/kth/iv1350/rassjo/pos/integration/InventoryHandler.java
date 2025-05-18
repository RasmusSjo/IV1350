package se.kth.iv1350.rassjo.pos.integration;

import se.kth.iv1350.rassjo.pos.integration.DTOs.*;
import se.kth.iv1350.rassjo.pos.integration.exceptions.ItemNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles operations related to the inventory system, including retrieving item details
 * and updating the inventory based on completed sales transactions.
 */
public class InventoryHandler {

    private final Map<ItemIdentifierDTO, ItemDTO> inventory;

    /**
     * Creates a new instance of the {@link InventoryHandler}.
     */
    InventoryHandler() {
        inventory = new HashMap<>();
        loadMockData();
    }

    /**
     * Retrieves detailed information about an item from the inventory system based on its identifier.
     *
     * @param itemId The {@link ItemIdentifierDTO} of the item for which information is requested.
     * @return An {@link ItemDTO} object containing the item's details, such as name, description,
     *         base price, and VAT rate.
     * @throws ItemNotFoundException if the item with the specified identifier is not found in the inventory.
     */
    public ItemDTO getItemInformation(ItemIdentifierDTO itemId) throws ItemNotFoundException {
        if (inventory.containsKey(itemId)) {
            return inventory.get(itemId);
        }
        throw new ItemNotFoundException("Item not found in inventory");
    }

    /**
     * Updates the inventory system with the details of a completed sale.
     *
     * @param saleInformation the {@link SaleDTO} object containing detailed
     *                        information about the sale.
     */
    public void updateInventory(SaleDTO saleInformation) {
    }

    private void loadMockData() {
        inventory.put(new ItemIdentifierDTO(10001),
                new ItemDTO(new ItemIdentifierDTO(10001),
                        "Milk 1L",
                        "Fresh whole milk, 1 liter",
                        new AmountDTO("19.95"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10002),
                new ItemDTO(new ItemIdentifierDTO(10002),
                        "Bread loaf",
                        "White bread loaf, ~500g",
                        new AmountDTO("37.90"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10003),
                new ItemDTO(new ItemIdentifierDTO(10003),
                        "Eggs (6-pack)",
                        "Free-range eggs, pack of 6",
                        new AmountDTO("44.95"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10004),
                new ItemDTO(new ItemIdentifierDTO(10004),
                        "Coffee 500g",
                        "Ground coffee, 500g bag",
                        new AmountDTO("97.95"),
                        new PercentageDTO(25)));

        inventory.put(new ItemIdentifierDTO(10005),
                new ItemDTO(new ItemIdentifierDTO(10005),
                        "Plastic bags (2L)",
                        "Plastic freezer bags, 2L, 70-pack",
                        new AmountDTO("15.95"),
                        new PercentageDTO(25)));

        inventory.put(new ItemIdentifierDTO(10006),
                new ItemDTO(new ItemIdentifierDTO(10006),
                        "Butter 200g",
                        "Salted butter, 200g stick",
                        new AmountDTO("24.95"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10007),
                new ItemDTO(new ItemIdentifierDTO(10007),
                        "Cheese 300g",
                        "Gouda cheese, 300g",
                        new AmountDTO("44.95"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10008),
                new ItemDTO(new ItemIdentifierDTO(10008),
                        "Apple (1 kg)",
                        "Red apples, 1 kg bag",
                        new AmountDTO("29.95"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10009),
                new ItemDTO(new ItemIdentifierDTO(10009),
                        "Tomahawk (900g)",
                        "Tomahawk steak, 900g, Swedish",
                        new AmountDTO("449.00"),
                        new PercentageDTO(12)));

        inventory.put(new ItemIdentifierDTO(10010),
                new ItemDTO(new ItemIdentifierDTO(10010),
                        "Orange juice 1L",
                        "Cold-pressed orange juice, 1L",
                        new AmountDTO("32.95"),
                        new PercentageDTO(12)));
    }
}
