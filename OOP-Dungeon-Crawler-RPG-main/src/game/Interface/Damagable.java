package game.Interface;

public interface Damagable {

	public abstract int  takeDamage (int amount);
	public abstract boolean isAlive();
	public abstract int getCurrentHealth();
}
