package game.gui;

import game.entity.Hero;
import game.item.Consumable;
import game.item.Equipment;
import game.item.Weapon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ShopFrame extends JDialog {

    private final Hero hero;
    private JComboBox<String> categoryBox;
    private JComboBox<String> itemBox;
    private JLabel goldLabel;
    private JButton buyBtn;
    private JButton closeBtn;

    public ShopFrame(JFrame owner, Hero hero) {
        super(owner, "Traveling Merchant", true); // modal dialog blocks battle
        this.hero = hero;
        initUI();
        setupKeyBindings();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        JLabel title = new JLabel("A traveling merchant approaches you from the shadows...");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 5, 5));
        goldLabel = new JLabel();
        updateGoldLabel();
        center.add(goldLabel);

        // category combo
        categoryBox = new JComboBox<>(new String[]{"Armor", "Weapons", "Consumables"});
        categoryBox.addActionListener(e -> populateItems());
        center.add(new LabeledPanel("Category:", categoryBox));

        // items combo
        itemBox = new JComboBox<>();
        populateItems();
        center.add(new LabeledPanel("Item:", itemBox));

        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buyBtn = new JButton("Buy");
        buyBtn.addActionListener(this::handleBuy);
        closeBtn = new JButton("Leave Shop");
        closeBtn.addActionListener(e -> dispose());

        bottom.add(buyBtn);
        bottom.add(closeBtn);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private void setupKeyBindings() {
        JRootPane rootPane = getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        // B: buy
        im.put(KeyStroke.getKeyStroke('B'), "buyAction");
        am.put("buyAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyBtn.doClick();
            }
        });

        // ESC: leave shop
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "closeShopAction");
        am.put("closeShopAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void updateGoldLabel() {
        goldLabel.setText("Your gold: " + hero.getCoins());
    }

    private void populateItems() {
        itemBox.removeAllItems();
        String category = (String) categoryBox.getSelectedItem();
        if (category == null) return;

        if (category.equals("Armor")) {
            Equipment[] armors = Equipment.equipments;
            for (Equipment eq : armors) {
                String text = eq.getName()
                        + " (Armor: " + (int) ((1.0 - eq.getArmorValue()) * 100) + "% dmg reduction"
                        + ") - " + eq.getPrice() + " Gold";
                itemBox.addItem(text);
            }
        } else if (category.equals("Weapons")) {
            Weapon[] weapons = Weapon.swords;
            for (Weapon w : weapons) {
                int price = w.getRarity() * 100;
                String text = w.getName()
                        + " (Damage: " + w.getDamage()
                        + ", Hit: " + w.getHitChance() + ") - "
                        + price + " Gold";
                itemBox.addItem(text);
            }
        } else if (category.equals("Consumables")) {
            Consumable[] cons = Consumable.consumableList;
            for (Consumable c : cons) {
                String text = c.getName()
                        + " (Heal: " + c.getHealAmount() + ") - "
                        + c.getPrice() + " Gold";
                itemBox.addItem(text);
            }
        }
    }

    private void handleBuy(ActionEvent e) {
        String category = (String) categoryBox.getSelectedItem();
        int index = itemBox.getSelectedIndex();
        if (category == null || index < 0) return;

        if (category.equals("Armor")) {
            buyArmor(index);
        } else if (category.equals("Weapons")) {
            buyWeapon(index);
        } else if (category.equals("Consumables")) {
            buyConsumable(index);
        }

        updateGoldLabel();
    }

    private void buyArmor(int idx) {
        Equipment[] armors = Equipment.equipments;
        if (idx < 0 || idx >= armors.length) return;
        Equipment chosen = armors[idx];

        Equipment currentEq = hero.getpEquipment();
        if (currentEq != null && currentEq.getName().equals(chosen.getName())) {
            JOptionPane.showMessageDialog(this,
                    "You already have " + chosen.getName() + " equipped!");
            return;
        }

        int price = chosen.getPrice();
        if (hero.getCoins() < price) {
            JOptionPane.showMessageDialog(this, "You don't have enough gold!");
            return;
        }

        hero.setCoins(hero.getCoins() - price);
        hero.setpEquipment(chosen);
        hero.setArmor(chosen.getArmorValue());
        JOptionPane.showMessageDialog(this,
                hero.getName() + " equipped " + chosen.getName()
                        + ". Gold left: " + hero.getCoins());
    }

    private void buyWeapon(int idx) {
        Weapon[] weapons = Weapon.swords;
        if (idx < 0 || idx >= weapons.length) return;
        Weapon chosen = weapons[idx];

        Weapon currentWpn = hero.getpWeapon();
        if (currentWpn != null && currentWpn.getName().equals(chosen.getName())) {
            JOptionPane.showMessageDialog(this,
                    "You already have " + chosen.getName() + " equipped!");
            return;
        }

        int price = chosen.getRarity() * 100;
        if (hero.getCoins() < price) {
            JOptionPane.showMessageDialog(this, "You don't have enough gold!");
            return;
        }

        hero.setCoins(hero.getCoins() - price);
        hero.setpWeapon(chosen);
        hero.setDamage(chosen.getDamage());
        JOptionPane.showMessageDialog(this,
                hero.getName() + " equipped " + chosen.getName()
                        + ". Gold left: " + hero.getCoins());
    }

    private void buyConsumable(int idx) {
        Consumable[] cons = Consumable.consumableList;
        if (idx < 0 || idx >= cons.length) return;
        Consumable chosen = cons[idx];

        int price = chosen.getPrice();
        if (hero.getCoins() < price) {
            JOptionPane.showMessageDialog(this, "You don't have enough gold!");
            return;
        }

        hero.setCoins(hero.getCoins() - price);
        hero.getInventory().add(chosen);
        JOptionPane.showMessageDialog(this,
                hero.getName() + " bought " + chosen.getName()
                        + ". Gold left: " + hero.getCoins());
    }

    /** Small helper panel for label + component. */
    private static class LabeledPanel extends JPanel {
        LabeledPanel(String label, JComponent comp) {
            super(new BorderLayout(5, 5));
            add(new JLabel(label), BorderLayout.WEST);
            add(comp, BorderLayout.CENTER);
        }
    }
}
