// ======================================
// EnemyEncounter.java - Combat Handler
// ======================================
package game.encounter;

import game.entity.Hero;

import game.entity.Enemy;


import game.core.GameManagerSys;
import game.entity.Enemy;
import game.entity.Hero;

public class EnemyEncounter extends Encounter {
    private Enemy enemy;
    
    public EnemyEncounter(String description, Enemy enemy) {
        super(description);
        this.enemy = enemy;
    }
    
    public Enemy getEnemy() {
        return enemy;
    }
    
    @Override
    public void trigger(GameManagerSys gm) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(description);
        System.out.println("=".repeat(50));
        System.out.println("Enemy: " + enemy.getName());
        System.out.println("Type: " + enemy.getType());
        System.out.println("HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
        System.out.println();
        
        Hero hero = gm.getPlayer();
        
        // Execute combat
        gm.handleCombat(hero, enemy);
        
        // Check outcome
        boolean heroAlive = hero.isAlive();
        boolean enemyAlive = enemy.isAlive();
        
        if (!heroAlive && !enemyAlive) {
            System.out.println("\nIn a final desperate exchange, both you and the " + 
                enemy.getName() + " fall!");
            System.out.println("You defeated the enemy... but at the cost of your life.");
            System.out.println("Game Over.");
        } 
        else if (!heroAlive) {
            System.out.println("\nYou have been defeated by the " + enemy.getName() + "...");
            System.out.println("Game Over.");
        } 
        else if (!enemyAlive) {
        	if (enemy.getId() == 1) {
        		System.out.println("\nThe goblin was defeated!");
        	}
        	else if (enemy.getId() == 2) {
        		System.out.println("\nThe skeleton turned into a pile of bones!");
        	}
        	else {
        		System.out.println("\nThe Orc fell with a loud thud!");
        	}
            
            // Let LootEncounter handle all rewards
            LootEncounter lootReward = new LootEncounter(enemy);
            lootReward.trigger(gm);
        } 
        else {
            System.out.println("\n[ERROR] Combat ended inconclusively.");
        }
    }
    
    private String getRarityName(int rarity) {
        switch (rarity) {
            case 1: return "Ordinary";
            case 2: return "Rare";
            case 3: return "Legendary";
            default: return "Mysterious";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Combat Encounter #%d: %s [HP: %d/%d]", 
            encounterId, enemy.getName(), enemy.getCurrentHealth(), enemy.getMaxHealth());
    }
}