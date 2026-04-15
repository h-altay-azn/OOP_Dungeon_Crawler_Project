package game.core;

import java.util.*;

import game.entity.Enemy;
import game.entity.Hero;
import game.item.Consumable;
import game.item.Item;
import game.item.Weapon;
import game.encounter.Encounter;
import game.encounter.EnemyEncounter;
import game.encounter.SpecialEncounter;

public class GameManagerSys {

    public static final String GAME_VERSION = "v.04";
    public static final int ENEMY_ENC = 1;
    public static final int SPEC_ENC = 2;
    public static boolean firstBattle = true;

    // single shared hero for console + GUI
    private static Hero player = new Hero();

    /* ================= BASIC ACCESSORS ================= */

    public static Hero getPlayer() {
        return player;
    }

    public static void setPlayer(Hero h) {
        player = h;
    }

    /* ================ NAME / ENCOUNTER CONTROL ================ */

    // console version of name asking
    public static void choosePlayerName() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Please enter your characters name: ");
        String inputName = sc.nextLine();
        player.setName(inputName);
        System.out.println("\nThe journey of " + player.getName() + " begins.");
    }

    public static int generateEncounterId() {
        if (firstBattle) {
            return ENEMY_ENC;
        }
        int roll = (int) (Math.random() * 4) + 1;
        return (roll == 4) ? SPEC_ENC : ENEMY_ENC;
    }

    public static void generateEncounter(Enemy[] enemies) {
        int id = generateEncounterId();
        Encounter enc;
        if (id == SPEC_ENC) {
            enc = new SpecialEncounter();
        } else {
            int idx = (int) (Math.random() * 6 + 1); //1 Orc 2-3 Skeleton >3 Goblin
            int choosenId;
            if (idx == 1) choosenId = 2;
            else if (idx <= 3) choosenId = 1;
            else choosenId = 0;

            if (firstBattle) {
                choosenId = 0;
                firstBattle = false;
            }

            Enemy base = enemies[choosenId];
            Enemy enemy = new Enemy(
                    base.getId(),
                    base.getRarity(),
                    base.getType(),
                    base.getCoinAmount(),
                    base.getDamage(),
                    base.getName(),
                    base.getMaxHealth()
            );
            enc = new EnemyEncounter("Battle!", enemy);
        }
        enc.trigger(new GameManagerSys());
    }

    public static void wait(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String getGameVersion() {
        return GAME_VERSION;
    }

    /* ===================== CONSOLE COMBAT ===================== */

    // original console-based combat; GUI uses its own version
    public static void handleCombat(Hero p, Enemy e) {
        Scanner sc = new Scanner(System.in);

        Weapon pWpn = p.getpWeapon();
        String pName = p.getName();
        int pDmg = pWpn.getDamage();
        int eDmg = e.getDamage();

        while (p.isAlive() && e.isAlive()) {

            // ===== get valid action (Attack / Item) =====
            String choice;
            while (true) {
                System.out.println("Will " + pName + " attack or use an item? (Attack/Item)");
                choice = sc.next();
                if (choice.equalsIgnoreCase("Attack") || choice.equalsIgnoreCase("A") ||
                        choice.equalsIgnoreCase("Item") || choice.equalsIgnoreCase("I")) {
                    break; // valid input
                } else {
                    System.out.println("Invalid choice! Please type 'Attack' or 'Item'.");
                }
            }

            boolean enemyGetsTurn = true;

            if (choice.equalsIgnoreCase("Attack") || choice.equalsIgnoreCase("A")) {
                System.out.println(pName + " lifted and swung their sword at the enemy.");
                int hitRandomValue = (int) (Math.random() * 100 + 1);
                if (p.getpWeapon().getHitChance() > hitRandomValue) {
                    int calcDamage = e.takeDamage(pDmg);
                    System.out.println(pName + " dealed " + calcDamage + " damage to the enemy!, Enemy health: "
                            + e.getCurrentHealth() + "/" + e.getMaxHealth());
                } else {
                    System.out.println("But " + pName + " missed!");
                }
            } else { // Item / I
                System.out.println(pName + " took a step back and looked inside their bag.");
                enemyGetsTurn = false;

                boolean hasConsumable = false;
                ArrayList<Item> inventory = p.getInventory();
                for (Item item : inventory) {
                    if (item instanceof Consumable) {
                        hasConsumable = true;
                        break;
                    }
                }

                if (!hasConsumable) {
                    System.out.println("But the bag was empty... No consumables available!");
                } else {
                    // ===== print consumables with 1‑based indices =====
                    List<Integer> consumableIndices = new ArrayList<>();
                    System.out.println("Choose a consumable to use:");
                    int displayIndex = 1;
                    for (int i = 0; i < inventory.size(); i++) {
                        if (inventory.get(i) instanceof Consumable) {
                            System.out.println(displayIndex + " - " + inventory.get(i).getName());
                            consumableIndices.add(i); // map displayIndex -> real inventory index
                            displayIndex++;
                        }
                    }

                    // ===== get valid item number =====
                    int chosenDisplayIndex;
                    while (true) {
                        if (!sc.hasNextInt()) {
                            System.out.println("Please enter a number.");
                            sc.next(); // discard invalid token
                            continue;
                        }

                        chosenDisplayIndex = sc.nextInt();
                        if (chosenDisplayIndex >= 1 && chosenDisplayIndex <= consumableIndices.size()) {
                            break;
                        } else {
                            System.out.println("Invalid choice! Enter a number between 1 and "
                                    + consumableIndices.size() + ".");
                        }
                    }

                    int choiceIndex = consumableIndices.get(chosenDisplayIndex - 1);
                    Consumable c = (Consumable) inventory.get(choiceIndex);
                    c.applyEffect(p);
                    inventory.remove(choiceIndex);
                    System.out.println("The item was consumed.");
                }
            }

            // If enemy is dead, stop before it can act
            if (e.getCurrentHealth() <= 0) {
                break;
            }

            // Enemy only attacks if it's still alive AND it gets a turn
            if (enemyGetsTurn) {
                int calcDamage = p.takeDamage(eDmg);
                System.out.println(p.getName() + " took " + calcDamage + " damage, Current health: "
                        + p.getCurrentHealth());
            }
        }
    }
}
