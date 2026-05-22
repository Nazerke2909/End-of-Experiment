package com.mygame.controller;

import com.mygame.model.Card;
import com.mygame.model.Enemy;
import com.mygame.model.Hero;
import com.mygame.model.PlayerProgress;
import com.mygame.view.CombatConfig;

import java.util.List;

public class CombatManager {
    public interface CombatStrategy {
        void executeAttack(Hero attacker, Card card, Enemy target, Hero[] heroes, List<Enemy> enemies);
    }

    public interface EnemyAttackStrategy {
        void executeEnemyAttack(Enemy attacker, Hero target);
    }

    public static class RabbitAttackStrategy implements CombatStrategy {
        @Override
        public void executeAttack(Hero attacker, Card card, Enemy target, Hero[] heroes, List<Enemy> enemies) {
            System.out.println(attacker.getName() + " throws acid bomb!");
            int damage = card.getDamage();
            for (Enemy enemy : enemies) {
                if (enemy.isAlive()) {
                    enemy.takeDamage(damage);
                    System.out.println("  -> " + enemy.getName() + " takes " + damage + " damage!");
                }
            }

            if (card.getType() == Card.CardType.ULTIMATE) {

                int healAmount = (int) (attacker.getMaxHp() * 0.5f);
                for (Hero hero : heroes) {
                    if (hero.isAlive()) {
                        hero.heal(healAmount);
                        System.out.println("  -> " + hero.getName() + " heals for " + healAmount + " HP!");
                    }
                }
            }
        }
    }
    public static class HorseAttackStrategy implements CombatStrategy {
        @Override
        public void executeAttack(Hero attacker, Card card, Enemy target, Hero[] heroes, List<Enemy> enemies) {
            System.out.println(attacker.getName() + " uses provoke on " + target.getName() + "!");
            target.takeDamage(card.getDamage());
            System.out.println("  -> " + target.getName() + " takes " + card.getDamage() + " damage!");

            if (card.getType() != Card.CardType.ULTIMATE) {
                target.addTaunt(attacker.getHeroIndex());
            } else {

                int shieldAmount = attacker.getDamage();
                for (int i = 0; i < heroes.length; i++) {
                    if (i != 1 && heroes[i].isAlive()) {
                        heroes[i].addShield(shieldAmount);
                        System.out.println("  -> " + heroes[i].getName() + " gains " + shieldAmount + " shield!");
                    }
                }
            }
        }
    }

    public static class DeerAttackStrategy implements CombatStrategy {
        @Override
        public void executeAttack(Hero attacker, Card card, Enemy target, Hero[] heroes, List<Enemy> enemies) {
            int damage = card.getDamage();
            if (card.getType() == Card.CardType.ULTIMATE) {
                damage = damage * 2; 
            }
            target.takeDamage(damage);
            System.out.println(attacker.getName() + " deals " + damage + " damage to " + target.getName() + "!");
        }
    }

    public static class DefaultEnemyAttackStrategy implements EnemyAttackStrategy {
        @Override
        public void executeEnemyAttack(Enemy attacker, Hero target) {
            System.out.println(attacker.getName() + " attacks " + target.getName()
                + " for " + attacker.getDamage() + " damage!");
        }
    }

    private final Hero[] heroes;
    private final List<Enemy> enemies;
    private final CardManager cardManager;
    private final TurnManager turnManager;
    private boolean battleEnded = false;
    private boolean victory = false;

    private final CombatStrategy[] heroStrategies;
    private final EnemyAttackStrategy defaultEnemyStrategy = new DefaultEnemyAttackStrategy();

    private int lastAttackerHeroIndex = -1;
    private int lastTargetEnemyIndex = -1;
    private Card.HeroType lastAttackerHeroType = null;

    private int lastAttackerEnemyIndex = -1;
    private int lastTargetHeroIndex = -1;

    public CombatManager(Hero[] heroes, List<Enemy> enemies) {
        this.heroes = heroes;
        this.enemies = enemies;
        this.cardManager = new CardManager(heroes);
        this.turnManager = new TurnManager();

        this.heroStrategies = new CombatStrategy[heroes.length];
        for (int i = 0; i < heroes.length; i++) {
            heroStrategies[i] = createStrategyForHero(i);
        }
    }

    private CombatStrategy createStrategyForHero(int heroIndex) {
        switch (heroIndex) {
            case 0: return new RabbitAttackStrategy(); 
            case 1: return new HorseAttackStrategy();   
            case 2: return new DeerAttackStrategy();    
            default: return new DeerAttackStrategy();
        }
    }

    public boolean playerAttack(int cardIndex, int enemyIndex, List<Card> hand) {
        if (battleEnded || !turnManager.isPlayerTurn()) return false;
        if (cardIndex < 0 || cardIndex >= hand.size()) return false;
        if (enemyIndex < 0 || enemyIndex >= enemies.size()) return false;

        Card card = hand.get(cardIndex);
        Enemy target = enemies.get(enemyIndex);
        if (!target.isAlive()) return false;

        int heroIndex = cardManager.getHeroIndexForCard(cardIndex);
        if (heroIndex == -1) return false;

        Hero attacker = heroes[heroIndex];
        boolean isUltimate = (card.getType() == Card.CardType.ULTIMATE);

        lastAttackerHeroIndex = heroIndex;
        lastTargetEnemyIndex = enemyIndex;
        lastAttackerHeroType = card.getHeroType();

        CombatStrategy strategy = heroStrategies[heroIndex];
        strategy.executeAttack(attacker, card, target, heroes, enemies);

        cardManager.removeCardFromSlot(cardIndex);

        if (!isUltimate) {
            cardManager.incrementCounter(heroIndex);
        }

        if (areAllEnemiesDead()) {
            victory = true;
            battleEnded = true;
            int reward = calculateReward();
            System.out.println("VICTORY! Reward: " + reward + " pills!");
            return true;
        }

        turnManager.endPlayerTurn();
        return true;
    }

    public boolean processEnemyAttack() {
        if (battleEnded || turnManager.isPlayerTurn()) return false;

        int enemyIdx = turnManager.getNextAliveEnemy(enemies);
        if (enemyIdx == -1) {
            if (areAllEnemiesDead()) {
                victory = true;
                battleEnded = true;
            }
            return false;
        }

        Enemy attacker = enemies.get(enemyIdx);

        int targetIdx;
        if (attacker.hasTaunt()) {
            targetIdx = attacker.getTauntingHeroIndex();
            if (targetIdx >= heroes.length || !heroes[targetIdx].isAlive()) {
                targetIdx = turnManager.getNextTarget(heroes);
            }
        } else {
            targetIdx = turnManager.getNextTarget(heroes);
        }

        if (targetIdx == -1) {
            victory = false;
            battleEnded = true;
            System.out.println("DEFEAT! All heroes are dead.");
            return false;
        }

        Hero target = heroes[targetIdx];
        target.takeDamage(attacker.getDamage());

        defaultEnemyStrategy.executeEnemyAttack(attacker, target);

        lastAttackerEnemyIndex = enemyIdx;
        lastTargetHeroIndex = targetIdx;

        attacker.decreaseTauntDuration();

        if (areAllHeroesDead()) {
            victory = false;
            battleEnded = true;
            System.out.println("DEFEAT! All heroes are dead.");
            return false;
        }

        turnManager.returnToPlayerTurn();
        return true;
    }

    public Hero[] getHeroes() { return heroes; }
    public List<Enemy> getEnemies() { return enemies; }
    public CardManager getCardManager() { return cardManager; }
    public TurnManager getTurnManager() { return turnManager; }
    public boolean isBattleEnded() { return battleEnded; }
    public boolean isVictory() { return victory; }
    public boolean isPlayerTurn() { return turnManager.isPlayerTurn(); }
    public int getLastAttackerHeroIndex() { return lastAttackerHeroIndex; }
    public int getLastTargetEnemyIndex() { return lastTargetEnemyIndex; }
    public Card.HeroType getLastAttackerHeroType() { return lastAttackerHeroType; }
    public int getLastAttackerEnemyIndex() { return lastAttackerEnemyIndex; }
    public int getLastTargetHeroIndex() { return lastTargetHeroIndex; }

    private boolean areAllEnemiesDead() {
        for (Enemy e : enemies) {
            if (e.isAlive()) return false;
        }
        return true;
    }

    private boolean areAllHeroesDead() {
        for (Hero h : heroes) {
            if (h.isAlive()) return false;
        }
        return true;
    }

    public int calculateReward() {
        return 0;
    }
}