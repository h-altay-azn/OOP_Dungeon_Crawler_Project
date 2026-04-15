// ============================
// LootEncounter.java - Shop
// ============================
package game.encounter;

import game.entity.Hero;

import game.item.Item;

import java.util.ArrayList;
import java.util.Scanner;

import game.core.GameMain;
import game.core.GameManagerSys;
import game.entity.Enemy;

import game.item.Consumable;
import game.item.Equipment;

import game.item.Weapon;


public class LootEncounter extends Encounter {
    private ArrayList<Item> loot;
    private int coins;
    
   
    public LootEncounter(Enemy enemy) {
        super("Collecting spoils of battle...");
        
        // Calculate coin reward based on enemy rarity
        double randomVal = (Math.random() * 5 + 8)/10;
        this.coins = (int) ( (double)(enemy.getCoinAmount()) * randomVal);
        
        // Get dropped item (may be null)
        Item droppedItem = enemy.dropItem();
        
        // Build loot list
        this.loot = new ArrayList<>();
        if (droppedItem != null) {
            this.loot.add(droppedItem);
        }
        
    }
    
    public ArrayList<Item> getLoot() {
        return new ArrayList<>(loot);
    }
    
    public int getCoins() {
        return coins;
    }
    
    @Override
    public void trigger(GameManagerSys gm) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(description);
        System.out.println("=".repeat(50));
        
        Hero hero = gm.getPlayer();
        boolean receivedAnything = false;
        
        int pCoins = hero.getCoins();
        
        // Process coins
        if (coins > 0) {
            hero.setCoins(pCoins + coins);
            System.out.println("Coins collected: " + coins);
            System.out.println("Total coins: " + hero.getCoins());
            receivedAnything = true;
        }

        // WIN CHECK: if reached, end encounter and game, do NOT open shop
        if (hero.getCoins() >= GameMain.WIN_GOLD_COND) { 
         
            return;                      
         }

        
        /**shop stuff starts here*/
        Shop("A traveling merchant approaches you from the shadows...");
    }
    public void Shop(String shopDescription) {
        System.out.println("" + "=".repeat(50));
        System.out.println(shopDescription);
        System.out.println("=".repeat(50));
        GameManagerSys.wait(1.0);

        Hero p = GameManagerSys.getPlayer();
        Scanner sc = new Scanner(System.in);

        boolean shopping = true;

        while (shopping) {
            System.out.println("\nHe offers you his wares.");
            System.out.println("Your gold: " + p.getCoins());
            GameManagerSys.wait(0.5);

            System.out.println("\nWhat are you interested in?");
            System.out.println("1 - Armor");
            System.out.println("2 - Weapons");
            System.out.println("3 - Consumable Items");
            System.out.println("Exit - Leave the shop");

            String input = sc.next().trim();

            if (input.equalsIgnoreCase("Exit")) {
                System.out.println("You declined the merchants offer.");
                shopping = false;
            } else if (input.equals("1")) {
                buyArmorMenu(sc, p);
            } else if (input.equals("2")) {
                buyWeaponMenu(sc, p);
            } else if (input.equals("3")) {
                buyItemMenu(sc, p);
            } else {
                System.out.println("The merchant doesn't understand your answer.");
            }
        }
    }
 
    private void buyArmorMenu(Scanner sc, Hero p) {
        Equipment[] armors = Equipment.equipments;

        boolean inArmorMenu = true;
        while (inArmorMenu) {
            System.out.println("\nAvailable armor:");
            for (int i = 0; i < armors.length; i++) {
                Equipment eq = armors[i];
                System.out.println((i + 1) + " - " + eq.getName() +
                        " (Armor: " + (int)((1. - eq.getArmorValue()) * 100) + "% damage reduction" +
                        ") - " + eq.getPrice() + " Gold");
            }
            System.out.println("Type number to buy or 'Exit' to go back.");

            String choice = sc.next().trim();
            if (choice.equalsIgnoreCase("Exit")) {
                inArmorMenu = false;          // go back to main shop
                continue;
            }

            int idx;
            try {
                idx = Integer.parseInt(choice) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice, please try again.");
                continue;                     // stay in armor menu
            }

            if (idx < 0 || idx >= armors.length) {
                System.out.println("Invalid choice, please try again.");
                continue;
            }

            Equipment chosen = armors[idx];

            // prevent buying same equipped armor 
            Equipment currentEq = p.getpEquipment();
            if (currentEq != null && currentEq.getName().equals(chosen.getName())) {
                System.out.println("You already have " + chosen.getName() + " equipped!");
                continue;                     // stay in armor menu, no gold spent
            }

            int price = chosen.getPrice();

            if (p.getCoins() < price) {
                System.out.println("You don't have enough gold!");
                continue;                     // stay in armor menu
            }

            p.setCoins(p.getCoins() - price);
            p.setpEquipment(chosen);
            p.setArmor(chosen.getArmorValue());
            System.out.println(p.getName() + " equipped " + chosen.getName() + ".");
            System.out.println("Gold left: " + p.getCoins());

        }
    }
// WEAPON
    private void buyWeaponMenu(Scanner sc, Hero p) {
        Weapon[] weapons = Weapon.swords;

        boolean inWeaponMenu = true;
        while (inWeaponMenu) {
            System.out.println("\nAvailable weapons:");
            for (int i = 0; i < weapons.length; i++) {
                Weapon w = weapons[i];
                System.out.println((i + 1) + " - " + w.getName() +
                        " (Damage: " + w.getDamage() +
                        ", Hit: " + w.getHitChance() + ")" +
                        " - " + (w.getRarity() * 100) + " Gold");
            }
            System.out.println("Type number to buy or 'Exit' to go back.");

            String choice = sc.next().trim();
            if (choice.equalsIgnoreCase("Exit")) {
                inWeaponMenu = false;         // go back to main shop
                continue;
            }

            int idx;
            try {
                idx = Integer.parseInt(choice) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice, please try again.");
                continue;
            }

            if (idx < 0 || idx >= weapons.length) {
                System.out.println("Invalid choice, please try again.");
                continue;
            }

            Weapon chosen = weapons[idx];

            // prevent buying same equipped weapon 
            Weapon currentWpn = p.getpWeapon();
            if (currentWpn != null && currentWpn.getName().equals(chosen.getName())) {
                System.out.println("You already have " + chosen.getName() + " equipped!");
                continue;                     // stay in weapon menu, no gold spent
            }

            int price = chosen.getRarity() * 100;

            if (p.getCoins() < price) {
                System.out.println("You don't have enough gold!");
                continue;
            }

            p.setCoins(p.getCoins() - price);
            p.setpWeapon(chosen);
            p.setDamage(chosen.getDamage());
            System.out.println(p.getName() + " equipped " + chosen.getName() + ".");
            System.out.println("Gold left: " + p.getCoins());

        }
    }

 // CONSUMABLES
    private void buyItemMenu(Scanner sc, Hero p) {
        Consumable[] consList = Consumable.consumableList;

        System.out.println("\nAvailable consumables:");
        for (int i = 0; i < consList.length; i++) {
            Consumable c = consList[i];
            System.out.println((i + 1) + " - " + c.getName() +
                               " (Heal: " + c.getHealAmount() +
                               ") - " + c.getPrice() + " Gold");
        }
        System.out.println("Type number to buy or 'Exit' to go back.");

        String input = sc.next().trim();
        if (input.equalsIgnoreCase("Exit")) return;

        int idx;
        try {
            idx = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        if (idx < 0 || idx >= consList.length) {
            System.out.println("Invalid choice.");
            return;
        }

        Consumable chosen = consList[idx];
        int price = chosen.getPrice();

        if (p.getCoins() < price) {
            System.out.println("You don't have enough gold!");
            return;
        }

        p.setCoins(p.getCoins() - price);
        p.getInventory().add(chosen);
        System.out.println(p.getName() + " bought " + chosen.getName());
        System.out.println("Gold left: " + p.getCoins());
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
        return String.format("Loot: %d coins, %d item(s)", coins, loot.size());
    }
}