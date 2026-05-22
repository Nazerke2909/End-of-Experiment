package com.mygame.controller;
import com.mygame.model.Card;
import com.mygame.model.Hero;
import com.mygame.view.CombatConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardManager {

    private final Hero[] heroes;
    private final int[] attackCounters;         
    private final Random random;

    private final Card[] persistentSlots;
    private final int[] persistentSlotHeroIndices;  
    private final Card.CardType[] persistentSlotTypes;

    public CardManager(Hero[] heroes) {
        this.heroes = heroes;
        this.attackCounters = new int[heroes.length];
        this.persistentSlots = new Card[CombatConfig.CARDS_SLOTS];
        this.persistentSlotHeroIndices = new int[CombatConfig.CARDS_SLOTS];
        this.persistentSlotTypes = new Card.CardType[CombatConfig.CARDS_SLOTS];
        this.random = new Random();
    }

    public List<Card> generateHand() {
        List<Card> hand = new ArrayList<>();
        List<Integer> aliveIndices = new ArrayList<>();

        for (int i = 0; i < heroes.length; i++) {
            if (heroes[i].isAlive()) {
                aliveIndices.add(i);
            }
        }

        if (aliveIndices.isEmpty()) {
            for (int slot = 0; slot < CombatConfig.CARDS_SLOTS; slot++) {
                hand.add(persistentSlots[slot]);
            }
            return hand;
        }
        List<Integer> heroesReadyForUltimate = new ArrayList<>();
        for (int heroIdx : aliveIndices) {
            if (attackCounters[heroIdx] >= CombatConfig.ULTIMATE_TRIGGER_COUNT) {
                boolean alreadyInSlot = false;
                for (int s = 0; s < CombatConfig.CARDS_SLOTS; s++) {
                    if (persistentSlots[s] != null
                            && persistentSlots[s].getType() == Card.CardType.ULTIMATE
                            && persistentSlotHeroIndices[s] == heroIdx) {
                        alreadyInSlot = true;
                        break;
                    }
                }
                if (!alreadyInSlot) {
                    heroesReadyForUltimate.add(heroIdx);
                }
            }
        }

        List<Integer> freeSlots = new ArrayList<>();
        for (int slot = 0; slot < CombatConfig.CARDS_SLOTS; slot++) {
            if (persistentSlots[slot] != null
                    && persistentSlots[slot].getType() == Card.CardType.ULTIMATE) {
                hand.add(persistentSlots[slot]);
            } else {
                freeSlots.add(slot);
                hand.add(null); 
            }
        }

        List<Integer> remainingFreeSlots = new ArrayList<>(freeSlots);
        for (int heroIdx : heroesReadyForUltimate) {
            if (remainingFreeSlots.isEmpty()) break;

            int slot = remainingFreeSlots.remove(0);
            Hero hero = heroes[heroIdx];
            Card.HeroType heroType = getHeroType(heroIdx);
            int ultraDmg = (int) (hero.getDamage() * CombatConfig.ULTIMATE_DAMAGE_MULTIPLIER);

            Card ultimateCard = new Card(hero.getName() + " ULTRA!", 0, Card.CardType.ULTIMATE, ultraDmg,
                    getUltimateDescription(heroIdx), heroType);

            persistentSlots[slot] = ultimateCard;
            persistentSlotHeroIndices[slot] = heroIdx;
            persistentSlotTypes[slot] = Card.CardType.ULTIMATE;
            attackCounters[heroIdx] = 0; 

            hand.set(slot, ultimateCard);
        }

        int[] heroCountInHand = new int[heroes.length];
        for (int slot = 0; slot < CombatConfig.CARDS_SLOTS; slot++) {
            if (persistentSlots[slot] != null) {
                int hIdx = persistentSlotHeroIndices[slot];
                if (hIdx >= 0 && hIdx < heroCountInHand.length) {
                    heroCountInHand[hIdx]++;
                }
            }
        }

        for (int slot : remainingFreeSlots) {
            List<Integer> available = new ArrayList<>();
            for (int idx : aliveIndices) {
                if (heroCountInHand[idx] < 2) {
                    available.add(idx);
                }
            }

            if (available.isEmpty()) {
                int minCount = Integer.MAX_VALUE;
                for (int idx : aliveIndices) {
                    if (heroCountInHand[idx] < minCount) {
                        minCount = heroCountInHand[idx];
                    }
                }
                for (int idx : aliveIndices) {
                    if (heroCountInHand[idx] == minCount) {
                        available.add(idx);
                    }
                }
            }

            int heroIdx = available.get(random.nextInt(available.size()));
            Hero hero = heroes[heroIdx];
            String description = getHeroDescription(heroIdx);
            Card.HeroType heroType = getHeroType(heroIdx);

            Card card = new Card(hero.getName() + " Attack", 0, Card.CardType.ATTACK, hero.getDamage(),
                    description, heroType);

            persistentSlots[slot] = card;
            persistentSlotHeroIndices[slot] = heroIdx;
            persistentSlotTypes[slot] = Card.CardType.ATTACK;

            hand.set(slot, card);
            heroCountInHand[heroIdx]++;
        }

        return hand;
    }

    public void removeCardFromSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < CombatConfig.CARDS_SLOTS) {
            persistentSlots[slotIndex] = null;
            persistentSlotHeroIndices[slotIndex] = -1;
            persistentSlotTypes[slotIndex] = null;
        }
    }

    public void incrementCounter(int heroIndex) {
        if (heroIndex >= 0 && heroIndex < attackCounters.length) {
            attackCounters[heroIndex]++;
            System.out.println(heroes[heroIndex].getName() + " attacks: "
                    + attackCounters[heroIndex] + "/" + CombatConfig.ULTIMATE_TRIGGER_COUNT);
        }
    }

    public int getAttackCounter(int heroIndex) {
        if (heroIndex >= 0 && heroIndex < attackCounters.length) {
            return attackCounters[heroIndex];
        }
        return 0;
    }

    public int[] getAttackCounters() {
        return attackCounters.clone();
    }

    public int getHeroIndexForCard(int cardIndex) {
        if (cardIndex >= 0 && cardIndex < persistentSlotHeroIndices.length) {
            return persistentSlotHeroIndices[cardIndex];
        }
        return -1;
    }

    public Card.CardType[] getLastCardTypes() {
        return persistentSlotTypes;
    }

    public int[] getLastCardHeroIndices() {
        return persistentSlotHeroIndices;
    }

    public void resetCounters() {
        for (int i = 0; i < attackCounters.length; i++) {
            attackCounters[i] = 0;
        }
        for (int i = 0; i < CombatConfig.CARDS_SLOTS; i++) {
            persistentSlots[i] = null;
            persistentSlotHeroIndices[i] = -1;
            persistentSlotTypes[i] = null;
        }
    }

    public static String getHeroDescription(int heroIdx) {
        switch (heroIdx) {
            case 0: 
                return "Throw acid bomb to deal damage to all enemies";
            case 1: 
                return "Deal damage and apply Taunt status for 1 turn";
            case 2: 
                return "Deal powerful damage to one selected target";
            default:
                return "Attack";
        }
    }

    public static String getUltimateDescription(int heroIdx) {
        switch (heroIdx) {
            case 0: 
                return "Deal 50% damage to all enemies and heal all allies by 50% of max HP";
            case 1: 
                return "Deal damage to one enemy and grant shield to allies";
            case 2: 
                return "Deal double damage to one selected enemy";
            default:
                return "Ultimate ability with increased damage";
        }
    }

    private Card.HeroType getHeroType(int heroIdx) {
        switch (heroIdx) {
            case 0:
                return Card.HeroType.RABBIT;
            case 1:
                return Card.HeroType.HORSE;
            case 2:
                return Card.HeroType.DEER;
            default:
                return Card.HeroType.RABBIT;
        }
    }
}