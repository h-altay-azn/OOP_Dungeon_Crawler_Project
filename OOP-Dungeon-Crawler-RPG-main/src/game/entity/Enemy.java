package game.entity;
import game.item.Consumable;

public class Enemy extends Entity {
    private int id;          
	private int rarity;
	private String type;
	private int coinAmount; 
	private int damage;
	// Enemy.java
	public Enemy(int id, int rarity, String type, int coin, int dmg,
	             String name, int maxHealth) {
	    this.setId(id);
	    this.name = name;
	    this.maxHealth = maxHealth;
	    this.currentHealth = maxHealth;
	    this.setRarity(rarity);
	    this.type = type;
	    this.coinAmount = coin;
	    this.damage = dmg;
	}

	public void setCurrentHealth(int hp) {
		currentHealth = hp;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public String chooseEnemyAction() { //choose action based on current health
		int randomInt = (int)(Math.random() * maxHealth + 1);
		if (randomInt > currentHealth) {
			int decision = (int)(Math.random() * 5);
			if (decision == 1) {
				return "R"; //run
			}
			else if (decision <= 3) {
				return "G"; //guard
			}
			return "F"; //fight
		}
		else {
			return "F";
		}
	}
	
	public int takeDamage(int amount)
	 {
		    int calcDamage = 0;

		    if (amount < 0) {                    // heal
		        currentHealth -= amount;         // amount is negative
		        if (currentHealth > maxHealth) currentHealth = maxHealth;
		        return 0;
		    }

		    // armor < 1 → reduces damage; armor > 1 → increases damage
		    double randomVal = (Math.random() * 5 + 8) / 10.0; // 0.8 – 1.3
		    calcDamage = (int)Math.max(1, Math.round(amount * armor * randomVal));

		    currentHealth -= calcDamage;
		    return calcDamage;
		}

	
	public Consumable dropItem() { //we can make an array of items and choose from there in a shop
		Consumable reward = Consumable.consumableList[0];
		return reward;
	}
	
	public int getCoinAmount() {
		return(coinAmount);
	}
	
	
	//getters setters
	public String getType() {
		return type;
	}
	
	@Override
	public int getCurrentHealth() {
		// TODO Auto-generated method stub
		return super.getCurrentHealth();
	}
	
	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return super.getMaxHealth();
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
