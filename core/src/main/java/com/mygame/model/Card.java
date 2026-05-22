package com.mygame.model;
public class Card {
    public enum CardType { ATTACK, ULTIMATE }
    public enum HeroType { DEER, HORSE, RABBIT }

    private String name;
    private int cost;
    private CardType type;
    private int damage;
    private String description;
    private HeroType heroType;

    public Card(String name, int cost, CardType type, int damage, String description, HeroType heroType) {
        this.name = name;
        this.cost = cost;
        this.type = type;
        this.damage = damage;
        this.description = description;
        this.heroType = heroType;
    }

    public static class Builder {
        private String name = "Unknown";
        private int cost = 0;
        private CardType type = CardType.ATTACK;
        private int damage = 0;
        private String description = "";
        private HeroType heroType = HeroType.RABBIT;

        public Builder name(String name) { this.name = name; return this; }
        public Builder cost(int cost) { this.cost = cost; return this; }
        public Builder type(CardType type) { this.type = type; return this; }
        public Builder damage(int damage) { this.damage = damage; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder heroType(HeroType heroType) { this.heroType = heroType; return this; }

        public Card build() {
            return new Card(name, cost, type, damage, description, heroType);
        }
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public CardType getType() { return type; }
    public int getDamage() { return damage; }
    public String getDescription() { return description; }
    public HeroType getHeroType() { return heroType; }
}