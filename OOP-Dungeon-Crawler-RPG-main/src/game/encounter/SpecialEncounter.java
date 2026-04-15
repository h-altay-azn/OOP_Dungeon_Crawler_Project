// ========================================
// SpecialEncounter.java - Special Events
// =========================================
package game.encounter;
import java.util.Random;

import game.core.GameManagerSys;
import game.entity.Hero;


public class SpecialEncounter extends Encounter {
    
  
    private static final String[] EVENT_NAMES = {
        "HEALING_SHRINE",
        "TRAP"
    };
    
    private String selectedEvent;
    
   
    public SpecialEncounter() {
        super("");  
        
   
        Random rand = new Random();
        this.selectedEvent = EVENT_NAMES[rand.nextInt(EVENT_NAMES.length)];

        this.description = getDescriptionForEvent(selectedEvent);
    }
    
 
    private String getDescriptionForEvent(String eventName) {
        switch (eventName) {
            case "HEALING_SHRINE":
                return "You stumble upon a mystical shrine...";
            case "TRAP":
                return "You hear a subtle click beneath your feet...";
            default:
                return "Something unusual happens...";
        }
    }
    
    @Override
    public void trigger(GameManagerSys gm) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(description);
        System.out.println("=".repeat(50));
        
        Hero hero = gm.getPlayer();
        
        // Execute the event that was selected during construction
        switch (selectedEvent) {
            case "HEALING_SHRINE":
                healingShrine(hero);
                break;
            case "TRAP":
                trap(hero);
                break;
                
            default:
                System.out.println("Nothing happens...");
        }
    }

    private void healingShrine(Hero hero) {
        System.out.println("A mystical healing shrine appears before you!");
        
        int healAmount = hero.getMaxHealth() / 2;
        hero.takeDamage(-healAmount);
        
        System.out.println("The shrine's magic restores your health!");
        System.out.println("HP restored: " + healAmount);
        System.out.println("Current HP: " + hero.getCurrentHealth() + 
            "/" + hero.getMaxHealth());
    }
    

    private void trap(Hero hero) {
        System.out.println("A hidden trap springs!");
        
        Random rand = new Random();
        int damage = 10 + rand.nextInt(21);  // 10 to 30 damage
        
        hero.takeDamage(damage);
        
        System.out.println("The trap deals " + damage + " damage!");
        System.out.println("Current HP: " + hero.getCurrentHealth() + 
            "/" + hero.getMaxHealth());
        
        if (hero.getCurrentHealth() <= 0) {
            System.out.println("\nYou died from the trap...");
            System.out.println("Game Over.");
        }
    }
    
    @Override
    public String toString() {
        return String.format("Special Encounter #%d: %s", encounterId, selectedEvent);
    }
}
