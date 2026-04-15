package game.item;
import java.util.ArrayList;

public abstract class Item {
    protected String name;
    protected int itemId;
    protected int rarity;

    public Item(String name, int itemId, int rarity) {
        this.name = name;
        this.itemId = itemId;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRarity() {
        return rarity;
    }

}
