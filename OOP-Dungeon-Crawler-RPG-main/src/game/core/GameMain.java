package game.core;

import game.entity.Enemy;
import game.entity.Hero;

public class GameMain {

    public static final int WIN_GOLD_COND = 500;

    public static void main(String[] args) {

        Enemy[] enemies = {
                new Enemy(1, 1, "Ordinary", 50, 8, "Goblin", 25),
                new Enemy(2, 2, "Rare",     75, 12, "Skeleton", 50),
                new Enemy(3, 3, "Legendary",100, 15, "Orc",      75)
        };

        Hero player = GameManagerSys.getPlayer();
        player.initializeDefault();

        System.out.println("To pass to the world of Mortals win " + WIN_GOLD_COND + " Gold!!\n");

        while (player.isAlive() && player.getCoins() < WIN_GOLD_COND) {
            GameManagerSys.generateEncounter(enemies);
            if (!player.isAlive()) {
                break;
            }
        }

        if (player.isAlive() && player.getCoins() >= WIN_GOLD_COND) {
            System.out.println("\n" + player.getName() + " got " + WIN_GOLD_COND
                    + " coins, you have won the game.\n");
            System.out.println("Thanks for playing!\nYou have played version: "
                    + GameManagerSys.getGameVersion());
        }
    }
}
