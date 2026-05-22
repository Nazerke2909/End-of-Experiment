package com.mygame.controller;
import com.mygame.model.Hero;
import com.mygame.model.PlayerProgress;

public class UpgradeController {

    public static boolean tryUpgrade(Hero hero) {
        int cost = hero.getUpgradeCost();
        if (cost == -1) return false; 

        PlayerProgress progress = PlayerProgress.getInstance();
        if (progress.spendCurrency(cost)) {
            return hero.upgrade();
        }
        return false;
    }
}
