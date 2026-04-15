// =====================================
// Encounter.java - Abstract Superclass
// ======================================
package game.encounter;


import game.core.GameManagerSys;

public abstract class Encounter {
    private static int totalEncounters = 0;
    
    protected String description;
    protected int encounterId;
    
    
    protected Encounter(String description) {
        this.description = description;
        this.encounterId = ++totalEncounters;
    }
    
    public static int getTotalEncounters() {
        return totalEncounters;
    }
    
    public static void resetEncounterCount() {
        totalEncounters = 0;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getEncounterId() {
        return encounterId;
    }
    
   
    public abstract void trigger(GameManagerSys gm);
    
    @Override
    public String toString() {
        return String.format("Encounter #%d: %s", encounterId, description);
    }
}