package game.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WinPage extends JDialog {

    private final int coins;

    public WinPage(JFrame owner, int coins) {
        super(owner, "Victory", true);
        this.coins = coins;
        initUI();
        setupKeyBindings();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JLabel title = new JLabel("GLORY!");
        title.setFont(new Font("Gurmukhi MN", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel msg = new JLabel("You have escaped the cursed dungeon!");
        msg.setFont(new Font("Gurmukhi MT", Font.BOLD, 16));
        msg.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel coinsLabel = new JLabel("You collected " + coins + " coins.");
        coinsLabel.setFont(new Font("Gurmukhi MT", Font.BOLD, 16));
        coinsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new GridLayout(3, 1, 5, 5));
        center.add(title);
        center.add(msg);
        center.add(coinsLabel);
        contentPane.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton playAgainBtn = new JButton("Play Again");
        JButton exitBtn = new JButton("Exit");

        playAgainBtn.addActionListener(this::handlePlayAgain);
        exitBtn.addActionListener(e -> System.exit(0));

        bottom.add(playAgainBtn);
        bottom.add(exitBtn);
        contentPane.add(bottom, BorderLayout.SOUTH);
    }

    private void setupKeyBindings() {
        JRootPane rootPane = getRootPane();
        InputMap im = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = rootPane.getActionMap();

        // P: play again
        im.put(KeyStroke.getKeyStroke('P'), "playAgainAction");
        am.put("playAgainAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handlePlayAgain(e);
            }
        });

        // ESC: exit game
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "exitAction");
        am.put("exitAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void handlePlayAgain(ActionEvent e) {
        dispose(); // close dialog
        SwingUtilities.invokeLater(GameBattleGUI::new);
        Window owner = getOwner();
        if (owner != null) {
            owner.dispose();
        }
    }
}
