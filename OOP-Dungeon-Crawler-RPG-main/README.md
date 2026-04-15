# Dungeon Crawler – Java OOP RPG Game

## Overview

Dungeon Crawler is a Java-based Object-Oriented RPG that you can play either in the console or with a simple GUI (made with Java Swing and AWT - Abstract Window Toolkit). It focuses on turn-based combat between a Hero (the player) and randomly spawned enemies, with loot, gold, items, and special events driving the progression. 

Enemies have different rarities. Rarer enemies have more HP, deal more damage, and reward more gold when defeated. After fights, a traveling merchant visits you to sell weapons, armor, and health potions. You can store and use healing items from your bag during battle.

---

## Gameplay

- You play as the **Hero** and fight one enemy at a time in turn-based combat.
- Enemies spawn randomly and have a rarity (for example, common, rare, legendary, etc.).
- The rarer the enemy:
  - The higher its HP.
  - The more damage it deals.
  - The more gold it drops when you defeat it.
- After a victory, you gain a randomized amount of gold based on the enemy’s rarity.
- You can then visit a traveling merchant (like a “dungeon Amazon” that appears after you defeat an enemy) and spend gold on:
  - Weapons (for higher damage).
  - Armor (for better protection).
  - Health potions (for healing). 
- Your bag stores health potions so you can use them mid-battle to recover HP.

### Dynamic Damage and Rarity

Damage and HP are not fixed. They are dynamically randomized using the rarity of the weapon, armor, and enemy:

- Rarer weapons → higher damage dealt by the hero.
- Rarer armor → less damage received by the hero.
- Rarer enemies → higher damage dealt and higher HP.

This creates more variety between runs and makes item and enemy rarity meaningful in combat. 

### Special Events

Between battles, there is a 25% (0.25) chance that a special event occurs.

- **Mysterious shrine of a lost hero**: restores 50 HP to the hero.
- **Mysterious trap**: deals 25 HP damage to the hero.

These events add risk and reward between fights and can change your situation between the next encounter. 

---

## Controls and User Experience

The game supports both console and GUI modes. 

### Console Mode

- Interaction happens through text prompts and keyboard input.
- You choose actions such as attacking, purchasing from the merchant, and using health potions purchased.
  
### GUI Mode

The GUI uses buttons, key presses, and mouse interactions:

- **Create Hero**:
  - Click the Create Hero button, or
  - Press `C`
- **Encounter**:
  - Click the Start Encounter / Next Encounter button, or
  - Press `E`
- **Attack**:
  - Click the Attack button, or
  - Press `A`, or
  - Double left-click in the main text box. 
- **Merchant / Items**:
  - Click the Items button, or
  - Press `I` to open the Mysterious Merchant. 
- **Search**:
  - Click the Search button, or
  - Press `S`. 

These event handlers make the game playable fully with the keyboard or a mix of mouse and keyboard. 

---

## Get Started

### Prerequisites

- Java Development Kit (JDK 8 or later) installed.
- A Java-compatible IDE (for example, IntelliJ IDEA, Eclipse, or VS Code with Java extensions).

### Running from an IDE

1. Clone the repository from Command Line:
   ```bash
   git clone https://github.com/Entrap-Io/OOP-Dungeon-Crawler-RPG.git
   ```
   
2. Open the project in your IDE and mark src as the source root if needed.

3. Build or compile the project so the IDE generates class files in its output directory (for example, bin or out).

4. Run the main class for the console version from core/GameMain

5. Run the main class for the GUI version from gui/GameBattleGUI to play with buttons and key bindings.

## Download

You can download the latest release of this project from Releases.

1. Go to the **Releases** page of this repository.
2. Download the zip file.
3. Unzip the file on your computer.
4. Run the game by double-clicking on the OOP-Dungeon-Crawler-RPG.jar file.

## Project Structure

The project follows a clear Object-Oriented design with separate packages for core logic, encounters, entities, GUI, interfaces, and items.

```text
src/
└─ game/
   ├─ core/
   │  ├─ GameMain.java        # Entry point for the game (console / launcher)
   │  └─ GameManagerSys.java  # Manages overall game flow and state
   ├─ encounter/
   │  ├─ Encounter.java       # Base class for encounters
   │  ├─ EnemyEncounter.java  # Handles hero vs enemy battle encounters
   │  ├─ LootEncounter.java   # Handles loot/gold/item reward encounters
   │  └─ SpecialEncounter.java# Handles special events (shrines, traps, etc.)
   ├─ entity/
   │  ├─ Enemy.java           # Enemy entity with HP, damage, rarity
   │  ├─ Entity.java          # Generic entity base class for shared stats
   │  └─ Hero.java            # Hero entity with HP, gold, equipment, bag
   ├─ gui/
   │  ├─ GameBattleGUI.java   # Main GUI battle screen and controls
   │  ├─ LosePage.java        # GUI screen shown when the hero loses
   │  ├─ ShopFrame.java       # GUI screen for the merchant/shop
   │  └─ WinPage.java         # GUI screen shown when the hero wins
   ├─ Interface/
   │  └─ Damagable.java       # Interface for objects that can take damage
   └─ item/
      ├─ Consumable.java      # Base class for consumable items (e.g., potions)
      ├─ Equipment.java       # Base class for equippable items (weapons/armor)
      ├─ Item.java            # Abstract parent for all items
      └─ Weapon.java          # Weapon item that increases hero damage
```

---

## Screenshots

Below is a visual overview of the game's features, from initial design to various in-game encounters and outcomes.

**OOP Game UML**  

  <img width="2448" height="1635" alt="Project UML" src="https://github.com/user-attachments/assets/151bc30c-0218-4b6a-8574-3d63c0a10b46" />

**Start Screen**

  <img width="893" height="570" alt="1" src="https://github.com/user-attachments/assets/67fc48da-dff4-4e1e-80bc-0610fbf1b0c1" />
 
**First Encounter Screen**  
 
  <img width="895" height="572" alt="2" src="https://github.com/user-attachments/assets/1ba2fe8b-ae48-440b-8096-c41573a34f51" />

**Traveling Merchant Screen**  

  <img width="897" height="571" alt="3" src="https://github.com/user-attachments/assets/899281c9-d439-44ee-b577-118955b30ba6" />

**Purchased Item Screen**  

  <img width="887" height="570" alt="4" src="https://github.com/user-attachments/assets/32c5bc6f-38e9-46c0-8296-7a12153950aa" />

**Purchased Armor Screen**  

  <img width="898" height="569" alt="8" src="https://github.com/user-attachments/assets/da48f815-3fc4-4051-9934-6bfe6ea30dc5" />

**Purchased Weapon Screen**  

  <img width="893" height="570" alt="9" src="https://github.com/user-attachments/assets/449b1051-48b0-43f0-bd5c-10b1f3b76907" />

**Player Details Screen**  

  <img width="897" height="571" alt="10" src="https://github.com/user-attachments/assets/c7f4e77e-456a-4900-babc-c54d8229d8a7" />

**Secret Shrine Screen**  

  <img width="900" height="571" alt="5" src="https://github.com/user-attachments/assets/990f4c75-8595-4a30-92cc-a7bb392b47f5" />

**Hidden Trap Screen**  

   <img width="894" height="573" alt="7" src="https://github.com/user-attachments/assets/bb19815a-6da5-43e6-acb3-c86951c34680" />

**Utilizing Heal Screen**
  
  <img width="897" height="571" alt="6" src="https://github.com/user-attachments/assets/50a3011b-de23-48eb-9706-3bc8a6f28339" />

**Legendary Enemy Screen**  

  <img width="895" height="563" alt="11" src="https://github.com/user-attachments/assets/bdd44ab4-06be-4854-9855-e02675b3de7b" />

**Search Screen**  

  <img width="888" height="572" alt="12" src="https://github.com/user-attachments/assets/291f6e40-4b0a-4415-a760-50e0724df476" />

**Tough Battle Screen**  

  <img width="893" height="566" alt="13" src="https://github.com/user-attachments/assets/7e730a36-ac9a-4e2f-b602-a9c41750f7d2" />

**Escaped Dungeon Screen**  

  <img width="896" height="570" alt="14" src="https://github.com/user-attachments/assets/2313e839-66fb-46cb-a5fe-eebf14cc36c9" />

**Perished Dungeon Screen**  

  <img width="890" height="571" alt="15" src="https://github.com/user-attachments/assets/07d67224-725b-404f-b8b3-60efb35efbcb" />

**Console Screen**

  <img width="890" height="575" alt="16" src="https://github.com/user-attachments/assets/e5effdc1-33fb-4d8d-8e1e-3b07a4f8a649" />
  
---

