package com.mygame.model;
public class Hero extends Entity {
    private int level = 1;
    private int ultimateDamage;
    private static final int MAX_HERO_LEVEL = 3;

    private static final int[][][] HERO_STATS = {
        {{40, 15, 30}, {60, 25, 50}, {80, 45, 80}},
        {{90, 10, 25}, {140, 18, 40}, {200, 30, 60}},
        {{55, 20, 40}, {85, 35, 70}, {115, 60, 110}}
    };

    private static final int[] UPGRADE_COSTS = {50, 120};

    private int heroIndex;

    public Hero(int heroIndex, String name) {
        super(name, HERO_STATS[heroIndex][0][0], HERO_STATS[heroIndex][0][1]);
        this.heroIndex = heroIndex;
        this.ultimateDamage = HERO_STATS[heroIndex][0][2];
    }

    public static Hero createHero(int heroIndex) {
        if (heroIndex < 0 || heroIndex >= getNames().length) {
            throw new IllegalArgumentException("Invalid hero index: " + heroIndex);
        }
        return new Hero(heroIndex, getNames()[heroIndex]);
    }

    public static Hero createHero(String name) {
        String[] names = getNames();
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(name)) {
                return new Hero(i, names[i]);
            }
        }
        throw new IllegalArgumentException("Unknown hero name: " + name);
    }

    @Override
    protected void beforeAttack() {
        System.out.println(getName() + " prepares to attack!");
    }

    public int getLevel() { return level; }
    public int getUltimateDamage() { return ultimateDamage; }
    public int getHeroIndex() { return heroIndex; }

    public boolean isMaxLevel() { return level >= MAX_HERO_LEVEL; }

    public int getUpgradeCost() {
        if (isMaxLevel()) return -1;
        return UPGRADE_COSTS[level - 1];
    }

    public boolean upgrade() {
        if (isMaxLevel()) return false;
        level++;
        int idx = level - 1;
        maxHp = HERO_STATS[heroIndex][idx][0];
        hp = maxHp;
        damage = HERO_STATS[heroIndex][idx][1];
        ultimateDamage = HERO_STATS[heroIndex][idx][2];
        return true;
    }

    public static String[] getNames() {
        return new String[]{"Sakura", "Gojo", "Yuki"};
    }
}