package com.mygame.controller;
import com.mygame.model.Enemy;
import com.mygame.model.Hero;
import com.mygame.view.CombatConfig;

import java.util.List;

public class TurnManager {

    public enum TurnPhase {
        PLAYER_TURN,
        ENEMY_TURN_DELAY,
        ENEMY_TURN_ATTACK,
        BATTLE_ENDED
    }

    private TurnPhase phase = TurnPhase.PLAYER_TURN;
    private int currentEnemyIndex = 0;  
    private int currentTargetIndex = 0; 
    private float enemyTimer = 0f;

    public TurnPhase getPhase() { return phase; }
    public boolean isPlayerTurn() { return phase == TurnPhase.PLAYER_TURN; }
    public boolean isBattleEnded() { return phase == TurnPhase.BATTLE_ENDED; }

    public void endPlayerTurn() {
        phase = TurnPhase.ENEMY_TURN_DELAY;
        enemyTimer = 0f;
    }

    public boolean updateEnemyTimer(float delta) {
        if (phase != TurnPhase.ENEMY_TURN_DELAY) return false;
        enemyTimer += delta;
        if (enemyTimer >= CombatConfig.ENEMY_ATTACK_DELAY) {
            phase = TurnPhase.ENEMY_TURN_ATTACK;
            enemyTimer = 0f;
            return true;
        }
        return false;
    }

    public int getNextAliveEnemy(List<Enemy> enemies) {
        int attempts = 0;
        while (attempts < enemies.size()) {
            int idx = currentEnemyIndex % enemies.size();
            currentEnemyIndex = (currentEnemyIndex + 1) % enemies.size();
            if (enemies.get(idx).isAlive()) return idx;
            attempts++;
        }
        return -1;
    }

        public int getNextTarget(Hero[] heroes) {
        int attempts = 0;
        while (attempts < heroes.length) {
            int heroIdx = CombatConfig.HERO_TARGET_ORDER[currentTargetIndex % CombatConfig.HERO_TARGET_ORDER.length];
            currentTargetIndex = (currentTargetIndex + 1) % CombatConfig.HERO_TARGET_ORDER.length;
            if (heroes[heroIdx].isAlive()) return heroIdx;
            attempts++;
        }
        return -1; 
    }

    public void returnToPlayerTurn() {
        phase = TurnPhase.PLAYER_TURN;
    }

    public void endBattle() {
        phase = TurnPhase.BATTLE_ENDED;
    }
}
