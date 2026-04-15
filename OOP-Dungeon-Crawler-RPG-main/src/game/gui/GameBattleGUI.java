package game.gui;

import game.core.GameMain;
import game.core.GameManagerSys;
import game.entity.Enemy;
import game.entity.Hero;
import game.encounter.LootEncounter;
import game.item.Consumable;
import game.item.Equipment;
import game.item.Item;
import game.item.Weapon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameBattleGUI extends JFrame {

    private Hero hero;
    private Enemy[] enemies;      // base enemies
    private Enemy currentEnemy;   // active enemy

    private JTextField heroNameField;
    private JButton createHeroBtn;
    private JButton encounterBtn;
    private JButton attackBtn;
    private JButton itemBtn;
    private JTextArea logArea;
    private JLabel heroStatusLabel;
    private JLabel enemyStatusLabel;

    private JFrame detailsFrame;
    private JTextArea detailsArea;

    // search UI
    private JTextField searchField;
    private JButton searchBtn;

    // Track hero actions in the current fight (attack or item)
    private int actionsThisFight = 0;
    // Allow at most one special event per fight
    private boolean specialUsedThisFight = false;

    public GameBattleGUI() {
        super("Dungeon Crawler RPG - Best RPG Game");

        initEnemies();
        initMainFrame();
        initDetailsFrame();
        setupGlobalKeyBindings();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /* ===================== INITIAL DATA ===================== */

    private void initEnemies() {
        // constructor: (id, rarity, type, maxHealth, dmg, name, coin)
        enemies = new Enemy[] {
                new Enemy(1, 1, "Ordinary", 25, 8, "Goblin", 50),
                new Enemy(2, 2, "Rare",     50, 12, "Skeleton", 75),
                new Enemy(3, 3, "Legendary",75, 15, "Orc",      100)
        };
    }
    
    private boolean isTypingInTextField() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .getFocusOwner() instanceof JTextField;
    }


    /* ===================== MAIN FRAME ===================== */

    private void initMainFrame() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(8, 8, 8, 8));
        setContentPane(root);

        JPanel top = new JPanel(new BorderLayout(5, 5));

        // hero creation
        JPanel heroPanel = new JPanel(new BorderLayout(5, 5));
        heroNameField = new JTextField();
        createHeroBtn = new JButton("Create Hero");
        createHeroBtn.addActionListener(e -> createHeroIfAllowed());

        heroNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleEnterKey();
                }
            }
        });

        heroPanel.add(new JLabel("Hero name:"), BorderLayout.WEST);
        heroPanel.add(heroNameField, BorderLayout.CENTER);
        heroPanel.add(createHeroBtn, BorderLayout.EAST);

        // buttons + search
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        encounterBtn = new JButton("Start Encounter");
        attackBtn = new JButton("Attack");
        itemBtn = new JButton("Use Item");
        JButton detailsBtn = new JButton("Details");

        encounterBtn.setEnabled(false);
        attackBtn.setEnabled(false);
        itemBtn.setEnabled(false);

        encounterBtn.addActionListener(e -> startEncounterIfAllowed());
        attackBtn.addActionListener(e -> heroAttack());
        itemBtn.addActionListener(e -> heroUseItem());
        detailsBtn.addActionListener(e -> openDetailsFrame());

        buttonPanel.add(encounterBtn);
        buttonPanel.add(attackBtn);
        buttonPanel.add(itemBtn);
        buttonPanel.add(detailsBtn);

        // search UI
        searchField = new JTextField(12);
        searchBtn = new JButton("Search Item");
        searchBtn.addActionListener(e -> searchInventory());
        buttonPanel.add(new JLabel("Search:"));
        buttonPanel.add(searchField);
        buttonPanel.add(searchBtn);

        top.add(heroPanel, BorderLayout.NORTH);
        top.add(buttonPanel, BorderLayout.SOUTH);
        root.add(top, BorderLayout.NORTH);

        // log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        logArea.setBorder(BorderFactory.createTitledBorder("Battle Log"));
        logArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // double‑click = attack, if allowed
                    if (hero != null && hero.isAlive()
                            && currentEnemy != null && currentEnemy.isAlive()
                            && attackBtn.isEnabled()) {
                        heroAttack();
                    }
                }
            }
        });

        root.add(new JScrollPane(logArea), BorderLayout.CENTER);

        // status
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        heroStatusLabel = new JLabel("Hero: -");
        enemyStatusLabel = new JLabel("Enemy: -");
        statusPanel.add(heroStatusLabel);
        statusPanel.add(enemyStatusLabel);
        root.add(statusPanel, BorderLayout.SOUTH);
    }

    /* ===================== GLOBAL KEY BINDING (SPACE, A, E, I, D, C) ===================== */

    private void setupGlobalKeyBindings() {
        JRootPane rootPane = getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        // SPACE or A: attack
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "attackAction");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "attackAction");
        am.put("attackAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTypingInTextField()) return;  // <-- added
                if (hero != null && hero.isAlive()
                        && currentEnemy != null && currentEnemy.isAlive()
                        && attackBtn.isEnabled()) {
                    heroAttack();
                }
            }
        });

        // E: new encounter
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "encounterAction");
        am.put("encounterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTypingInTextField()) return;  // <-- added
                if (encounterBtn.isEnabled()
                        && (currentEnemy == null || !currentEnemy.isAlive())) {
                    startEncounterIfAllowed();
                }
            }
        });

        // I: use item
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "itemAction");
        am.put("itemAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTypingInTextField()) return; 
                if (hero != null && hero.isAlive()
                        && currentEnemy != null && currentEnemy.isAlive()
                        && itemBtn.isEnabled()) {
                    heroUseItem();
                }
            }
        });

        // D: details
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "detailsAction");
        am.put("detailsAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTypingInTextField()) return;
                openDetailsFrame();
            }
        });

        // C: create hero
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "createHeroAction");
        am.put("createHeroAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isTypingInTextField()) return;
                if (hero == null || !hero.isAlive()) {
                    createHeroIfAllowed();
                }
            }
        });
    }

    /* ===================== ENTER KEY LOGIC ===================== */

    private void handleEnterKey() {
        // No hero or dead hero -> Enter creates hero
        if (hero == null || !hero.isAlive()) {
            if (createHeroBtn.isEnabled()) {
                createHeroIfAllowed();
            }
            return;
        }
        // Hero alive, no active enemy -> Enter starts encounter
        if ((currentEnemy == null || !currentEnemy.isAlive())
                && encounterBtn.isEnabled()) {
            startEncounterIfAllowed();
        }
    }

    /* ===================== DETAILS FRAME ===================== */

    private void initDetailsFrame() {
        detailsFrame = new JFrame("Hero / Inventory");
        detailsFrame.setSize(500, 400);
        detailsFrame.setLocationRelativeTo(this);

        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        p.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshDetails());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(refreshBtn);
        p.add(bottom, BorderLayout.SOUTH);

        detailsFrame.setContentPane(p);
    }

    private void openDetailsFrame() {
        if (!detailsFrame.isVisible()) {
            refreshDetails();
            detailsFrame.setVisible(true);
        } else {
            refreshDetails();
            detailsFrame.toFront();
        }
    }

    /* ===================== HERO CREATION ===================== */

    private void createHeroIfAllowed() {
        if (hero != null && hero.isAlive()) {
            JOptionPane.showMessageDialog(this,
                    "Hero is already alive. You can only create a new hero after death.");
            return;
        }

        String name = heroNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a hero name.");
            return;
        }

        hero = new Hero();
        hero.setName(name);
        hero.setCoins(0);

        logArea.setText("");
        logArea.append("To pass to the world of Mortals win "
                + GameMain.WIN_GOLD_COND + " Gold!!\n\n");
        logArea.append("The journey of " + hero.getName() + " begins...\n");

        encounterBtn.setEnabled(true);
        encounterBtn.setText("Start Encounter");
        attackBtn.setEnabled(false);
        itemBtn.setEnabled(false);
        currentEnemy = null;
        actionsThisFight = 0;
        specialUsedThisFight = false;
        updateStatusLabels();
    }

    /* ===================== ENCOUNTER START ===================== */

    private void startEncounterIfAllowed() {
        if (hero == null || !hero.isAlive()) {
            JOptionPane.showMessageDialog(this, "Create a living hero first.");
            return;
        }
        if (currentEnemy != null && currentEnemy.isAlive()) {
            JOptionPane.showMessageDialog(this,
                    "Finish the current encounter before starting a new one.");
            return;
        }
        if (hero.getCoins() >= GameMain.WIN_GOLD_COND) {
            showWinPage();
            return;
        }

        // New battle → reset per-fight special tracking
        actionsThisFight = 0;
        specialUsedThisFight = false;

        // Enemy selection logic exactly as console
        int idx = (int) (Math.random() * 6 + 1); // 1 Orc, 2-3 Skeleton, >3 Goblin
        int choosenId;
        if (idx == 1) choosenId = 2;
        else if (idx <= 3) choosenId = 1;
        else choosenId = 0;

        if (GameManagerSys.firstBattle) {
            choosenId = 0;           // goblin first
            GameManagerSys.firstBattle = false;
        }

        Enemy base = enemies[choosenId];
        currentEnemy = new Enemy(
                base.getId(),
                base.getRarity(),
                base.getType(),
                base.getMaxHealth(),
                base.getDamage(),
                base.getName(),
                base.getCoinAmount()
        );

        logArea.append("\n============================================\n");
        logArea.append("Battle! Enemy: " + currentEnemy.getName()
                + " (" + currentEnemy.getType() + ")\n");
        logArea.append("HP: " + currentEnemy.getCurrentHealth() + "/"
                + currentEnemy.getMaxHealth() + "\n");
        logArea.append("============================================\n");

        attackBtn.setEnabled(true);
        itemBtn.setEnabled(true);
        encounterBtn.setText("Next Encounter");
        updateStatusLabels();
    }

    private boolean ensureInBattle() {
        if (hero == null || !hero.isAlive()) {
            JOptionPane.showMessageDialog(this, "Hero dead or not created.");
            return false;
        }
        if (currentEnemy == null || !currentEnemy.isAlive()) {
            JOptionPane.showMessageDialog(this, "No active enemy. Start an encounter.");
            return false;
        }
        return true;
    }

    /* ===================== ACTIONS: ATTACK / ITEM ===================== */

    private void heroAttack() {
        if (!ensureInBattle()) return;

        Weapon w = hero.getpWeapon();
        if (w == null) {
            JOptionPane.showMessageDialog(this, "Hero has no weapon.");
            return;
        }

        logArea.append(hero.getName() + " attacks with " + w.getName() + "...\n");
        int hitRandom = (int) (Math.random() * 100 + 1);
        if (w.getHitChance() > hitRandom) {
            int dmg = currentEnemy.takeDamage(w.getDamage());
            logArea.append("Hit! " + dmg + " damage.\n");
        } else {
            logArea.append("But the attack missed!\n");
        }

        actionsThisFight++; // one more hero action in this fight
        afterHeroAction(true);
    }

    private void heroUseItem() {
        if (!ensureInBattle()) return;

        ArrayList<Item> inv = hero.getInventory();
        ArrayList<Consumable> cons = new ArrayList<>();
        for (Item it : inv) {
            if (it instanceof Consumable c) cons.add(c);
        }
        if (cons.isEmpty()) {
            logArea.append("No consumables in inventory.\n");
            actionsThisFight++;
            afterHeroAction(false);
            return;
        }

        String[] options = new String[cons.size()];
        for (int i = 0; i < cons.size(); i++) options[i] = cons.get(i).getName();

        String chosen = (String) JOptionPane.showInputDialog(
                this,
                "Choose a consumable:",
                "Use Item",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (chosen == null) {
            // canceled → enemy still gets a chance to act
            actionsThisFight++;
            afterHeroAction(true);
            return;
        }

        int indexInInv = -1;
        for (int i = 0; i < inv.size(); i++) {
            if (inv.get(i) instanceof Consumable c &&
                    c.getName().equals(chosen)) {
                indexInInv = i;
                break;
            }
        }
        if (indexInInv >= 0) {
            Consumable c = (Consumable) inv.get(indexInInv);
            c.applyEffect(hero);
            inv.remove(indexInInv);
            logArea.append("Used " + c.getName() + ".\n");
        }
        actionsThisFight++;
        // item‑turn: enemy does NOT attack this round
        afterHeroAction(false);
    }

    /* ===================== AFTER HERO ACTION ===================== */

    private void afterHeroAction(boolean enemyActs) {
        updateStatusLabels();

        // Enemy dead → loot + shop
        if (!currentEnemy.isAlive()) {
            logArea.append("Enemy defeated!\n");

            LootEncounter loot = new LootEncounter(currentEnemy);
            int reward = loot.getCoins();
            hero.setCoins(hero.getCoins() + reward);
            logArea.append("Coins collected: " + reward +
                    " (Total: " + hero.getCoins() + ")\n");

            if (hero.getCoins() >= GameMain.WIN_GOLD_COND) {
                showWinPage();
                return;
            }

            // Shop
            attackBtn.setEnabled(false);
            itemBtn.setEnabled(false);
            encounterBtn.setEnabled(false);

            ShopFrame shop = new ShopFrame(this, hero);
            shop.setVisible(true); // modal

            encounterBtn.setEnabled(true);
            currentEnemy = null;
            actionsThisFight = 0;
            specialUsedThisFight = false;
            updateStatusLabels();
            return;
        }

        // Enemy still alive
        if (enemyActs) {
            boolean enemyTurnReplacedBySpecial = false;

            // Special event constraints:
            // - Not first hero action in fight
            // - Only one special per fight
            if (actionsThisFight > 1 && !specialUsedThisFight) {
                int roll = (int) (Math.random() * 10) + 1; // 1..10
                if (roll <= 2) { // 20% chance
                    logArea.append("[A special event interrupts the fight!]\n");
                    triggerSpecialEncounterGUI(); // shrine or trap
                    specialUsedThisFight = true;
                    enemyTurnReplacedBySpecial = true;
                    if (hero == null || !hero.isAlive()) {
                        updateStatusLabels();
                        return;
                    }
                }
            }

            if (!enemyTurnReplacedBySpecial) {
                int dmg = hero.takeDamage(currentEnemy.getDamage());
                logArea.append(currentEnemy.getName() + " hits back for "
                        + dmg + " damage.\n");
            }
        }

        if (!hero.isAlive()) {
            logArea.append("You have been defeated by the "
                    + currentEnemy.getName() + "... Game Over.\n");
            showLosePage();
        }

        updateStatusLabels();
    }

    /* ===== GUI version of SpecialEncounter (same events as console) ===== */

    private void triggerSpecialEncounterGUI() {
        String[] events = {"HEALING_SHRINE", "TRAP"};
        String selectedEvent = events[(int) (Math.random() * events.length)];

        String description;
        switch (selectedEvent) {
            case "HEALING_SHRINE":
                description = "You stumble upon a mystical shrine...";
                break;
            case "TRAP":
                description = "You hear a subtle click beneath your feet...";
                break;
            default:
                description = "Something unusual happens...";
        }

        logArea.append("\n============================================\n");
        logArea.append(description + "\n");
        logArea.append("============================================\n");

        if (hero == null) return;

        switch (selectedEvent) {
            case "HEALING_SHRINE": {
                int healAmount = hero.getMaxHealth() / 2;
                hero.takeDamage(-healAmount);
                logArea.append("A mystical healing shrine restores your health!\n");
                logArea.append("HP restored: " + healAmount + "\n");
                logArea.append("Current HP: " + hero.getCurrentHealth()
                        + "/" + hero.getMaxHealth() + "\n");
                JOptionPane.showMessageDialog(this,
                        "The shrine restores " + healAmount + " HP!\n"
                                + "Current HP: " + hero.getCurrentHealth()
                                + "/" + hero.getMaxHealth());
                break;
            }
            case "TRAP": {
                int damage = 10 + (int) (Math.random() * 21); // 10–30
                hero.takeDamage(damage);
                logArea.append("A hidden trap springs and deals " + damage + " damage!\n");
                logArea.append("Current HP: " + hero.getCurrentHealth()
                        + "/" + hero.getMaxHealth() + "\n");

                if (hero.getCurrentHealth() <= 0) {
                    logArea.append("\nYou died from the trap...\nGame Over.\n");
                    JOptionPane.showMessageDialog(this,
                            "The trap dealt " + damage + " damage.\n"
                                    + "You died from the trap... Game Over.");
                    showLosePage();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "The trap dealt " + damage + " damage.\n"
                                    + "Current HP: " + hero.getCurrentHealth()
                                    + "/" + hero.getMaxHealth());
                }
                break;
            }
            default:
                break;
        }

        updateStatusLabels();
    }

    /* ===================== WIN / LOSE PAGES ===================== */

    private void showWinPage() {
        attackBtn.setEnabled(false);
        itemBtn.setEnabled(false);
        encounterBtn.setEnabled(false);

        WinPage win = new WinPage(this, hero.getCoins());
        win.setVisible(true);
    }

    private void showLosePage() {
        attackBtn.setEnabled(false);
        itemBtn.setEnabled(false);
        encounterBtn.setEnabled(false);

        LosePage lose = new LosePage(this, hero != null ? hero.getCoins() : 0);
        lose.setVisible(true);
    }

    /* ===================== INVENTORY SEARCH ===================== */

    private void searchInventory() {
        if (hero == null) {
            JOptionPane.showMessageDialog(this,
                    "Create a hero first before searching inventory.");
            return;
        }

        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please type an item name to search.");
            return;
        }

        String qLower = query.toLowerCase();
        ArrayList<Item> inv = hero.getInventory();
        ArrayList<Item> matches = new ArrayList<>();

        for (Item it : inv) {
            if (it.getName().toLowerCase().contains(qLower)) {
                matches.add(it);
            }
        }

        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No items matching \"" + query + "\" were found in your inventory.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Found ").append(matches.size())
              .append(" item(s) matching \"").append(query).append("\":\n\n");
            int i = 1;
            for (Item it : matches) {
                sb.append(i++).append(") ")
                  .append(it.getName())
                  .append("  [id: ").append(it.getItemId())
                  .append(", rarity: ").append(it.getRarity())
                  .append("]\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    /* ===================== DETAILS / STATUS ===================== */

    private void updateStatusLabels() {
        if (hero != null) {
            heroStatusLabel.setText("Hero: " + hero.getName()
                    + " | HP " + hero.getCurrentHealth() + "/" + hero.getMaxHealth()
                    + " | Coins " + hero.getCoins());
        } else {
            heroStatusLabel.setText("Hero: -");
        }

        if (currentEnemy != null && currentEnemy.isAlive()) {
            enemyStatusLabel.setText("Enemy: " + currentEnemy.getName()
                    + " (" + currentEnemy.getType() + ")"
                    + " | HP " + currentEnemy.getCurrentHealth()
                    + "/" + currentEnemy.getMaxHealth());
        } else {
            enemyStatusLabel.setText("Enemy: -");
        }
    }

    private void refreshDetails() {
        if (hero == null) {
            detailsArea.setText("No hero.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Hero: ").append(hero.getName()).append("\n");
        sb.append("HP: ").append(hero.getCurrentHealth()).append("/")
                .append(hero.getMaxHealth()).append("\n");
        sb.append("Coins: ").append(hero.getCoins()).append("\n");

        Weapon w = hero.getpWeapon();
        sb.append("Weapon: ");
        if (w != null) {
            sb.append(w.getName()).append(" (Dmg ")
                    .append(w.getDamage()).append(", Hit ")
                    .append(w.getHitChance()).append(")\n");
        } else {
            sb.append("none\n");
        }

        Equipment eq = hero.getpEquipment();
        sb.append("Armor: ");
        if (eq != null) {
            int reduction = (int) ((1.0 - eq.getArmorValue()) * 100);
            sb.append(eq.getName()).append(" (")
                    .append(reduction).append("% damage reduction)\n");
        } else {
            sb.append("none\n");
        }

        sb.append("\nInventory:\n");
        ArrayList<Item> inv = hero.getInventory();
        if (inv.isEmpty()) {
            sb.append("  [empty]\n");
        } else {
            int i = 1;
            for (Item it : inv) {
                sb.append("  ").append(i++).append(") ")
                        .append(it.getName()).append("\n");
            }
        }
        detailsArea.setText(sb.toString());
    }

    /* ===================== MAIN ===================== */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameBattleGUI::new);
    }
}
