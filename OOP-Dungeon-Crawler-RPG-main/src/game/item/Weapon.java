package game.item;

import game.entity.Entity;

import java.util.ArrayList;

public class Weapon extends Item {

    private int damage;
    private double hitChance;
    ArrayList<Item> arr = new ArrayList<Item>();
    
    
   
    
    public  Weapon(String name, int itemId, int rarity, int damage, int hitChance) {
        super(name, itemId, rarity);
        this.damage = damage;
        this.hitChance = hitChance;
        this.arr=arr;
       
    
    }
    
    public static Weapon[] swords = {new Weapon("Ordinary Sword",1,1,10,80),
    		new Weapon("Rare Sword",2,2,15,90),
    		new Weapon("Legendary Sword",3,3,20,95)};

    public int getDamage() {
        return damage;
    }

    public double getHitChance() {
        return hitChance;
    }
    
    public Weapon[] getSwords() {
		return swords;
	}
}
