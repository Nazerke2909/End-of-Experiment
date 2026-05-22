package com.mygame.model;
public class Enemy extends Entity {
    private int rewardPills;
    private int tauntingHeroIndex = -1;
    private int tauntDuration = 0;

    public Enemy(String name, int hp, int damage, int rewardPills) {
        super(name, hp, damage);
        this.rewardPills = rewardPills;
    }

    public static Enemy createEnemy(String type) {
        switch (type.toLowerCase()) {
            case "bat":     return new Enemy("Bat", 30, 5, 10);
            case "spider":  return new Enemy("Spider", 50, 8, 15);
            case "wolf":    return new Enemy("Wolf", 60, 12, 20);
            case "bear":    return new Enemy("Bear", 100, 20, 30);
            case "golem":   return new Enemy("Golem", 150, 25, 40);
            case "slime":   return new Enemy("Slime", 20, 3, 5);
            default:
                System.out.println("Unknown enemy type: " + type + ", using Slime");
                return new Enemy("Slime", 20, 3, 5);
        }
    }

    @Override
    protected int calculateDamage() {
        double variance = 0.8 + Math.random() * 0.4;
        return (int) Math.round(damage * variance);
    }

    public int getRewardPills() { return rewardPills; }

    public void addTaunt(int heroIndex) {
        this.tauntingHeroIndex = heroIndex;
        this.tauntDuration = 1;
    }

    public void removeTaunt() {
        tauntingHeroIndex = -1;
        tauntDuration = 0;
    }

    public int getTauntingHeroIndex() { return tauntingHeroIndex; }

    public boolean hasTaunt() { return tauntingHeroIndex != -1 && tauntDuration > 0; }

    public void decreaseTauntDuration() {
        if (hasTaunt()) {
            tauntDuration--;
            if (tauntDuration <= 0) removeTaunt();
        }
    }
}