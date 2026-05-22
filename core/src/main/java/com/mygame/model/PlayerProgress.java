package com.mygame.model;
public class PlayerProgress {
    private static PlayerProgress instance;

    private int currency = 0;
    private int maxUnlockedLevel = 1; 
    private static final int MAX_LEVEL = 3;

    private PlayerProgress() {}

    public static PlayerProgress getInstance() {
        if (instance == null) instance = new PlayerProgress();
        return instance;
    }

    public int getCurrency() { return currency; }
    public void addCurrency(int amount) { currency = Math.max(0, currency + amount); }
    public boolean spendCurrency(int amount) {
        if (currency >= amount) { currency -= amount; return true; }
        return false;
    }

    public int getMaxUnlockedLevel() { return maxUnlockedLevel; }
    public boolean isLevelUnlocked(int level) { return level <= maxUnlockedLevel; }
    public void unlockNextLevel() {
        if (maxUnlockedLevel < MAX_LEVEL) {
            maxUnlockedLevel++;
            System.out.println("Level " + maxUnlockedLevel + " unlocked!");
        }
    }
    public void reset() {
        currency = 0;
        maxUnlockedLevel = 1;
    }
}
