package game.entity;

import game.item.Equipment;
import game.item.Item;
import game.item.Weapon;

import java.util.*;

public class Hero extends Entity {
	private int damage;
	private int coins;
	private ArrayList<Item> inventory = new ArrayList(); //for consumables
	private Weapon pWeapon;
	private Equipment pEquipment;
	
	public void initializeDefault() {
		healthMultiplier = 1.0;
		maxHealth = (int) (100 * healthMultiplier);
		currentHealth = maxHealth;
		System.out.printf("Please enter your characters name: ");
		Scanner sc = new Scanner(System.in);
		name = sc.nextLine().trim();
		System.out.println("\nThe journey of " + name + " begins...\n");
		armor = 1;
		coins = 0;
		
		damage = 10;
	}

	
	public Hero() {
		healthMultiplier = 1.0;
		maxHealth = (int) (100 * healthMultiplier);
		currentHealth = maxHealth;
		name = "Warrior"; //default
		armor = 1;
		coins = 0;
		Weapon starter = Weapon.swords[0];
		pWeapon = starter;
		
		damage = starter.getDamage();
	    inventory.add(starter);   

	}
	
	public void attack(Enemy E) {
		int takenDmg = fightRng(damage);
		E.setCurrentHealth(E.currentHealth - takenDmg);
		if (E.currentHealth <= 0) { //check if enemy is dead or not!
			System.out.println("Enemy was slain!\n");
			coins += E.getCoinAmount();
			System.out.println("The enemy dropped " + E.getCoinAmount() + " coins.");
			System.out.println("Current coins: " + coins +"\n");
			return;
		}
		System.out.println("Enemy took " + takenDmg + " damage. " + "New Enemy Health: " + E.currentHealth);
		
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

	
	
	public void takeDamage(Enemy E) {
		
		int takenDmg = fightRng(E.getDamage());
		currentHealth -= takenDmg;
		System.out.println(name + " took " + takenDmg + " damage. " + "New player health: " + currentHealth);
	}
	
	public int fightRng(int damage) {
		int randomInt = (int)(Math.random() * damage + (damage));
		return randomInt;
	}
	
	

	public Item searchInventory(String itemName) {
	    for (Item item : inventory) {
	        if (item.getName().equalsIgnoreCase(itemName)) {
	            return item;
	        }
	    }
	    return null;
	}  // will be used later in the GUI part.

	
	
	
	//setters getters
	public int getCoins() {
		return coins;
	}
	public void setCoins(int coins) {
		this.coins = coins;
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
	
	public ArrayList<Item> getInventory() {
		return inventory;
	}
	public Weapon getpWeapon() {
		return pWeapon;
	}

	public void setpWeapon(Weapon pWeapon) {
		this.pWeapon = pWeapon;
	}


	public Equipment getpEquipment() {
		return pEquipment;
	}


	public void setpEquipment(Equipment pEquipment) {
		this.pEquipment = pEquipment;
	}


	public int getDamage() {
		return damage;
	}
	


	public void setDamage(int damage) {
		this.damage = damage;
	}

}
