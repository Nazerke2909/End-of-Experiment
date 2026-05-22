package com.mygame.controller;
import com.mygame.model.Enemy;
import com.mygame.model.PlayerProgress;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    public static List<Enemy> loadEnemies(int level) {
        List<Enemy> enemies = new ArrayList<>();
                switch (level) {
            case 1:
                enemies.add(new Enemy("Bat", 30, 5, 10));
                enemies.add(new Enemy("Bat", 30, 5, 10));
                enemies.add(new Enemy("Spider", 50, 8, 15));
                break;
            case 2:
                enemies.add(new Enemy("Wolf", 60, 12, 20));
                enemies.add(new Enemy("Wolf", 60, 12, 20));
                enemies.add(new Enemy("Bear", 100, 20, 30));
                break;
            case 3:
                enemies.add(new Enemy("Golem", 150, 25, 40));
                enemies.add(new Enemy("Golem", 150, 25, 40));
                break;
            default:
                enemies.add(new Enemy("Slime", 20, 3, 5));
        }
        return enemies;
    }

    public static void unlockNextLevel() {
        PlayerProgress.getInstance().unlockNextLevel();
    }
}
