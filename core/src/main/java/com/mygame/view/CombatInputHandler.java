package com.mygame.view;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygame.model.Enemy;

import java.util.List;

public class CombatInputHandler {

    public enum ClickTarget {
        NONE,
        CARD,
        ENEMY,
        MENU_BUTTON,
        CARD_INFO
    }

    public static class ClickResult {
        public ClickTarget target = ClickTarget.NONE;
        public int index = -1;
        public int selectedCardIdx = -1;
        public int hoveredEnemyIdx = -1;

        public ClickResult() {}
        public ClickResult(ClickTarget target, int index, int selectedCardIdx) {
            this.target = target;
            this.index = index;
            this.selectedCardIdx = selectedCardIdx;
        }
    }

    private final Viewport viewport;
    private int selectedCardIndex = -1;
    private int draggedCardIndex = -1;
    private float draggedCardX = 0;
    private float draggedCardY = 0;
    private int hoveredEnemyIndex = -1;
    private int showCardInfoIndex = -1;

    public CombatInputHandler(Viewport viewport) {
        this.viewport = viewport;
    }

    public int getSelectedCardIndex() { return selectedCardIndex; }
    public void clearSelectedCard() { selectedCardIndex = -1; }
    public int getDraggedCardIndex() { return draggedCardIndex; }
    public float getDraggedCardX() { return draggedCardX; }
    public float getDraggedCardY() { return draggedCardY; }
    public int getHoveredEnemyIndex() { return hoveredEnemyIndex; }
    public int getShowCardInfoIndex() { return showCardInfoIndex; }
    public void setShowCardInfoIndex(int index) { showCardInfoIndex = index; }
    public void clearCardInfo() { showCardInfoIndex = -1; }

    public ClickResult handleTouch(int handSize, List<Enemy> enemies) {
        ClickResult result = new ClickResult();

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPos);
        float mouseX = touchPos.x;
        float mouseY = touchPos.y;

        hoveredEnemyIndex = -1;
        if (draggedCardIndex != -1) {
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                if (!enemy.isAlive()) continue;

                float enemyX = CombatConfig.ENEMY_X[i];
                float enemyY = CombatConfig.CANVAS_HEIGHT - CombatConfig.ENEMY_Y_CANVAS[i] - CombatConfig.ENEMY_H[i];

                if (isInside(mouseX, mouseY, enemyX, enemyY, CombatConfig.ENEMY_W[i], CombatConfig.ENEMY_H[i])) {
                    hoveredEnemyIndex = i;
                    break;
                }
            }
        }

        if (Gdx.input.justTouched()) {
            if (isInside(mouseX, mouseY,
                    CombatConfig.MENU_BTN_X,
                    CombatConfig.CANVAS_HEIGHT - CombatConfig.MENU_BTN_Y_CANVAS - CombatConfig.MENU_BTN_H,
                    CombatConfig.MENU_BTN_W,
                    CombatConfig.MENU_BTN_H)) {
                return new ClickResult(ClickTarget.MENU_BUTTON, 0, selectedCardIndex);
            }

            for (int i = 0; i < handSize && i < CombatConfig.CARDS_SLOTS; i++) {
                float cardX = CombatConfig.CARD_X[i];
                float cardY = CombatConfig.CARDS_Y;

                if (isInside(mouseX, mouseY, cardX, cardY, CombatConfig.CARD_W, CombatConfig.CARD_H)) {
                    draggedCardIndex = i;
                    draggedCardX = mouseX;
                    draggedCardY = mouseY;
                    selectedCardIndex = i;
                    result.target = ClickTarget.CARD;
                    result.index = i;
                    result.selectedCardIdx = i;
                    return result;
                }
            }

            if (draggedCardIndex != -1 && hoveredEnemyIndex != -1) {
                int heldCard = draggedCardIndex;
                draggedCardIndex = -1;
                selectedCardIndex = -1;
                result.target = ClickTarget.ENEMY;
                result.index = hoveredEnemyIndex;
                result.selectedCardIdx = heldCard;
                return result;
            }

            draggedCardIndex = -1;
            selectedCardIndex = -1;
        }

        if (draggedCardIndex != -1 && (Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT))) {
            draggedCardX = mouseX;
            draggedCardY = mouseY;
        } else if (draggedCardIndex != -1 && hoveredEnemyIndex != -1) {
            int heldCard = draggedCardIndex;
            draggedCardIndex = -1;
            selectedCardIndex = -1;
            result.target = ClickTarget.ENEMY;
            result.index = hoveredEnemyIndex;
            result.selectedCardIdx = heldCard;
            return result;
        } else if (draggedCardIndex != -1 && !Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
            draggedCardIndex = -1;
        }

        return result;
    }

    private boolean isInside(float px, float py, float rx, float ry, float rw, float rh) {
        return px >= rx && px <= rx + rw && py >= ry && py <= ry + rh;
    }
}
