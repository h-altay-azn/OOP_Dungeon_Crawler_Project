package game.item;
import game.entity.Entity;
import game.entity.Hero;
public class Consumable extends Item {

    private int healAmount;
    private int price;


    public Consumable(String name, int itemId, int price, int healAmount, int staminaBoost, int rarity) {
        super(name, itemId, rarity);
        this.price = price;
        this.healAmount = healAmount;
    }

    public void applyEffect(Entity user) {
        user.takeDamage(-healAmount);

        System.out.println(user.getName() + " used " + name +
                " (Heal: " + healAmount + ")");
        
        System.out.println("After using " + name +" current health of our proud warior " + user.getName()+ " is now "+ user.getCurrentHealth()+".");// recently added 
    }
    
    public static Consumable[] consumableList= {new Consumable("Healing Potion",1,15,25,10,1),
    		new Consumable("Healing Flask",2,50,50,10,1),
    		new Consumable("Potion of Endurance",3,90,80,10,1)};
    
    public int getPrice() {
		return price;
	}
    public int getHealAmount() {
		return healAmount;
	}
}
