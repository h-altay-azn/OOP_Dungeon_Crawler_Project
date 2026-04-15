package game.item;
import game.entity.Entity;
public class Equipment extends Item {

    private double armorValue;
    private String slot;
    private int price;

    public Equipment(String name, int itemId, int rarity, double armorValue) {
        super(name, itemId, rarity);
        this.armorValue = armorValue;
        this.price = rarity * 100;      // price based on rarity
    }

    public static Equipment[] equipments = {
        new Equipment("Ordinary Vest",   1, 1, 0.85),  // 100 gold
        new Equipment("Rare Vest",     2, 2, 0.75),  // 200 gold
        new Equipment("Legendary Vest",  3, 3, 0.6)   // 300 gold
    };

    public double getArmorValue() {
        return armorValue;
    }

    public int getPrice() {
        return price;
    }

    public String getSlot() {
        return slot;
    }
}
