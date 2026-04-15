package game.entity;

import game.Interface.Damagable;

public abstract class Entity implements Damagable{
	protected String name;
	protected int maxHealth;
	protected int currentHealth;
	protected double healthMultiplier;
	
	protected double armor = 1;
	protected boolean equipedWeapon; //add them later
	
	public String getName() {
		return name;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public double getHealthMultiplier() {
		return healthMultiplier;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	@Override
	public boolean isAlive() {
		if (currentHealth > 0) return true;
		return false;
	}

	public double getArmor() {
		return armor;
	}

	public void setArmor(double armor) {
		this.armor = armor;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
